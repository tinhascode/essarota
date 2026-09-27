package io.github.tinhascode.essarota.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

    private final RateLimiterService rateLimiterService;
    private final ClientIpResolver clientIpResolver;

    public RateLimitingFilter(RateLimiterService rateLimiterService, ClientIpResolver clientIpResolver) {
        this.rateLimiterService = rateLimiterService;
        this.clientIpResolver = clientIpResolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (isRotaIsenta(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = clientIpResolver.resolveClientIp(request);
        RateLimiterService.RateLimitResult resultado = rateLimiterService.avaliarRequisicao(
                clientIp,
                uri,
                request.getMethod()
        );

        response.setHeader("X-RateLimit-Limit", String.valueOf(resultado.limite()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(resultado.restante()));
        response.setHeader("X-RateLimit-Reset", String.valueOf(resultado.segundosParaReset()));

        if (!resultado.permitido()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Retry-After", String.valueOf(resultado.segundosParaReset()));

            if (resultado.emJail()) {
                log.warn("Rejeição 403 (IP em Jail) para IP '{}' tentando acessar '{}'", clientIp, uri);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("""
                        {
                            "status": 403,
                            "error": "Forbidden",
                            "message": "Seu endereço de IP foi temporariamente bloqueado por excesso de requisições maliciosas ou abusivas. Tente novamente mais tarde.",
                            "retryAfterSeconds": %d,
                            "path": "%s"
                        }
                        """.formatted(resultado.segundosParaReset(), uri));
            } else {
                log.warn("Rejeição 429 (Too Many Requests) para IP '{}' tentando acessar '{}'", clientIp, uri);
                response.setStatus(429);
                response.getWriter().write("""
                        {
                            "status": 429,
                            "error": "Too Many Requests",
                            "message": "Limite de taxa excedido. Por favor, aguarde antes de enviar novas requisições.",
                            "retryAfterSeconds": %d,
                            "path": "%s"
                        }
                        """.formatted(resultado.segundosParaReset(), uri));
            }
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRotaIsenta(String uri) {
        if (uri == null) return false;
        return uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs");
    }
}
