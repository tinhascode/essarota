package io.github.tinhascode.essarota.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de sucesso da autenticação contendo o token JWT")
public record LoginResponse(
        @Schema(description = "Token JWT para ser informado no cabeçalho Authorization", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tipo
) {
    public static LoginResponse bearer(String token) {
        return new LoginResponse(token, "Bearer");
    }
}
