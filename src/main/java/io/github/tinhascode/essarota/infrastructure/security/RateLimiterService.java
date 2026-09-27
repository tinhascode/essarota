package io.github.tinhascode.essarota.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterService {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterService.class);

    public static final int LIMITE_ROTAS_AUTENTICACAO = 10;     
    public static final int LIMITE_ROTAS_PADRAO = 100;          
    public static final int MAX_VIOLACOES_PARA_BLOQUEIO = 3;    
    public static final long TEMPO_JAIL_SEGUNDOS = 900;         

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, Integer> violacoesPorIp = new ConcurrentHashMap<>();
    private final Map<String, Instant> jailPorIp = new ConcurrentHashMap<>();

    public record RateLimitResult(boolean permitido, boolean emJail, int limite, int restante, long segundosParaReset) {}

    public RateLimitResult avaliarRequisicao(String ip, String uri, String metodo) {
        Instant agora = Instant.now();

        Instant expiracaoJail = jailPorIp.get(ip);
        if (expiracaoJail != null) {
            if (agora.isBefore(expiracaoJail)) {
                long segundosRestantes = expiracaoJail.getEpochSecond() - agora.getEpochSecond();
                log.warn("IP '{}' bloqueado (Jail). Faltam {} segundos para expirar.", ip, segundosRestantes);
                return new RateLimitResult(false, true, 0, 0, segundosRestantes);
            } else {
                // Jail expirou
                jailPorIp.remove(ip);
                violacoesPorIp.remove(ip);
                buckets.remove(gerarChaveBucket(ip, true));
                buckets.remove(gerarChaveBucket(ip, false));
                log.info("IP '{}' liberado do Jail de Rate Limit.", ip);
            }
        }

        boolean isRotaSensivel = isRotaCritica(uri, metodo);
        int capacidadeMaxima = isRotaSensivel ? LIMITE_ROTAS_AUTENTICACAO : LIMITE_ROTAS_PADRAO;
        String chaveBucket = gerarChaveBucket(ip, isRotaSensivel);

        TokenBucket bucket = buckets.computeIfAbsent(chaveBucket, k -> new TokenBucket(capacidadeMaxima, agora));

        synchronized (bucket) {
            bucket.reabastecer(agora, capacidadeMaxima);

            if (bucket.tokens >= 1) {
                bucket.tokens--;
                long segundosParaReset = 60 - ((agora.getEpochSecond() - bucket.janelaInicio.getEpochSecond()) % 60);
                return new RateLimitResult(true, false, capacidadeMaxima, bucket.tokens, Math.max(1, segundosParaReset));
            } else {
                // Limite estourado
                int violacoes = violacoesPorIp.merge(ip, 1, (atual, incremento) -> atual + incremento);
                log.warn("IP '{}' atingiu taxa máxima para chave '{}'. Violações acumuladas: {}", ip, chaveBucket, violacoes);

                if (violacoes >= MAX_VIOLACOES_PARA_BLOQUEIO) {
                    Instant jailAte = agora.plusSeconds(TEMPO_JAIL_SEGUNDOS);
                    jailPorIp.put(ip, jailAte);
                    log.error("IP '{}' colocado em JAIL até {} após atingir {} violações de taxa.", ip, jailAte, violacoes);
                    return new RateLimitResult(false, true, capacidadeMaxima, 0, TEMPO_JAIL_SEGUNDOS);
                }

                long segundosParaReset = 60 - ((agora.getEpochSecond() - bucket.janelaInicio.getEpochSecond()) % 60);
                return new RateLimitResult(false, false, capacidadeMaxima, 0, Math.max(1, segundosParaReset));
            }
        }
    }

    private String gerarChaveBucket(String ip, boolean isSensivel) {
        return ip + (isSensivel ? ":auth" : ":default");
    }

    private boolean isRotaCritica(String uri, String metodo) {
        if (uri == null) return false;
        if (uri.contains("/auth/login")) return true;
        return uri.equals("/api/v1/usuarios") && "POST".equalsIgnoreCase(metodo);
    }

    private static class TokenBucket {
        int tokens;
        Instant janelaInicio;

        TokenBucket(int capacidade, Instant inicio) {
            this.tokens = capacidade;
            this.janelaInicio = inicio;
        }

        void reabastecer(Instant agora, int capacidade) {
            long segundosPassados = agora.getEpochSecond() - janelaInicio.getEpochSecond();
            if (segundosPassados >= 60) {
                this.tokens = capacidade;
                this.janelaInicio = agora;
            }
        }
    }
}
