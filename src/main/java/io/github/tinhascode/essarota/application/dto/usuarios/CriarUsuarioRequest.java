package io.github.tinhascode.essarota.application.dto.usuarios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de um novo usuário no sistema")
public record CriarUsuarioRequest(
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "E-mail único de acesso", example = "joao.silva@email.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @Schema(description = "Senha de acesso (mínimo de 6 caracteres)", example = "senhaSegura123")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @Schema(description = "Número do WhatsApp com DDI e DDD (opcional)", example = "+5511999998888")
        @Pattern(
                regexp = "^$|^\\+?[1-9]\\d{8,14}$",
                message = "Formato de telefone/whatsapp inválido. Ex: +5511999999999"
        )
        String telefoneWhatsapp,

        @Schema(description = "Token do dispositivo (FCM) para push notifications (opcional)", example = "fcm_token_device_abc123")
        String deviceToken
) {
}
