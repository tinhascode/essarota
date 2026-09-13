package io.github.tinhascode.essarota.application.dto.usuarios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de um usuário existente")
public record AtualizarUsuarioRequest(
        @Schema(description = "Nome atualizado do usuário", example = "João da Silva Santos")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "E-mail do usuário", example = "joao.santos@email.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @Schema(description = "Nova senha (opcional, informe apenas se desejar alterar a senha atual)", example = "novaSenhaSegura456")
        @Size(min = 6, max = 100, message = "A senha deve ter no mínimo 6 caracteres se informada")
        String senha,

        @Schema(description = "Número do WhatsApp atualizado", example = "+5511988887777")
        @Pattern(
                regexp = "^$|^\\+?[1-9]\\d{8,14}$",
                message = "Formato de telefone/whatsapp inválido. Ex: +5511999999999"
        )
        String telefoneWhatsapp,

        @Schema(description = "Token do dispositivo atualizado", example = "fcm_token_device_novo456")
        String deviceToken
) {
}
