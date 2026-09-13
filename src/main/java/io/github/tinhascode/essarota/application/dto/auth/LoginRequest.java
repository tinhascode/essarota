package io.github.tinhascode.essarota.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação no sistema")
public record LoginRequest(
        @Schema(description = "E-mail cadastrado", example = "joao.silva@email.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @Schema(description = "Senha cadastrada", example = "senhaSegura123")
        @NotBlank(message = "A senha é obrigatória")
        String senha
) {
}
