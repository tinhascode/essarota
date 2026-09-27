package io.github.tinhascode.essarota.application.dto.notificacoes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Representação detalhada de uma notificação enviada")
public record NotificacaoResponse(
        @Schema(description = "Identificador único da notificação (UUID)", example = "b11a5b82-8bfb-4dc7-a1cb-9e0a05b38dcb")
        UUID id,

        @Schema(description = "Identificador do usuário destinatário (UUID)", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185")
        UUID usuarioId,

        @Schema(description = "Identificador do alerta disparado (UUID)", example = "8a32d1ef-1980-4965-a82f-8557b4260efb")
        UUID alertaId,

        @Schema(description = "Canal de envio", example = "WHATSAPP")
        String canal,

        @Schema(description = "Data e hora do envio da notificação", example = "2026-09-27T17:31:00Z")
        Instant enviadoEm
) {
}
