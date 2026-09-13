package io.github.tinhascode.essarota.infrastructure.security;

import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService implements TokenService {

    private final SecretKey secretKey;
    private final long expirationHours;

    public JwtService(
            @Value("${jwt.secret:umaChaveSecretaMuitoSeguraComMaisDe32CaracteresParaHMAC256}") String secret,
            @Value("${jwt.expiration-hours:24}") long expirationHours
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationHours = expirationHours;
    }

    @Override
    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expiracao = agora.plus(expirationHours, ChronoUnit.HOURS);

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("usuarioId", usuario.getId().toString())
                .claim("nome", usuario.getNome())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(expiracao))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean isTokenValido(String token) {
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    @Override
    public UUID extrairUsuarioId(String token) {
        String usuarioIdStr = extrairClaims(token).get("usuarioId", String.class);
        return usuarioIdStr != null ? UUID.fromString(usuarioIdStr) : null;
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
