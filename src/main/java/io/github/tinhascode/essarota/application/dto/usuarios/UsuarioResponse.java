package io.github.tinhascode.essarota.application.dto.usuarios;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Representação pública dos dados do usuário")
public record UsuarioResponse(
        @Schema(description = "Identificador único (UUID)", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185")
        UUID id,

        @Schema(description = "Nome do usuário", example = "João da Silva")
        String nome,

        @Schema(description = "E-mail de acesso", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Telefone para WhatsApp", example = "+5511999998888")
        String telefoneWhatsapp,

        @Schema(description = "Token do dispositivo móvel para notificações push", example = "fcm_token_device_abc123")
        String deviceToken
) {
}
