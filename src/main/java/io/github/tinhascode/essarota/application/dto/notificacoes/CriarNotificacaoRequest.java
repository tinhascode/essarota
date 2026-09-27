package io.github.tinhascode.essarota.application.dto.notificacoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Dados para envio e registro de uma nova notificação")
public record CriarNotificacaoRequest(
        @Schema(description = "Identificador do usuário que receberá o alerta (UUID)", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185")
        @NotNull(message = "O ID do usuário destinatário é obrigatório")
        UUID usuarioId,

        @Schema(description = "Identificador do alerta que disparou a notificação (UUID)", example = "8a32d1ef-1980-4965-a82f-8557b4260efb")
        @NotNull(message = "O ID do alerta é obrigatório")
        UUID alertaId,

        @Schema(description = "Canal de envio da mensagem", example = "WHATSAPP", allowableValues = {"WHATSAPP", "PUSH"})
        @NotBlank(message = "O canal de envio é obrigatório")
        @Size(min = 2, max = 30, message = "O canal deve conter entre 2 e 30 caracteres")
        String canal
) {
}
