package io.github.tinhascode.essarota.application.dto.trajetos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Representação detalhada de um trajeto do usuário")
public record TrajetoResponse(
        @Schema(description = "Identificador único do trajeto (UUID)", example = "d94b0d74-c089-4e78-9e45-9858f9a26312")
        UUID id,

        @Schema(description = "Identificador do usuário proprietário (UUID)", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185")
        UUID usuarioId,

        @Schema(description = "Origem do trajeto", example = "Terminal Metrô Santana, São Paulo - SP")
        String origem,

        @Schema(description = "Destino do trajeto", example = "Avenida Paulista, 1000, São Paulo - SP")
        String destino,

        @Schema(description = "Tempo estimado de percurso em minutos", example = "45")
        Integer tempoEstimadoMinutos
) {
}
