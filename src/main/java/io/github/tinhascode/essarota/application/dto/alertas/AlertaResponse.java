package io.github.tinhascode.essarota.application.dto.alertas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Representação detalhada de um alerta de transporte")
public record AlertaResponse(
        @Schema(description = "Identificador único do alerta (UUID)", example = "8a32d1ef-1980-4965-a82f-8557b4260efb")
        UUID id,

        @Schema(description = "Identificador da linha relacionada (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID linhaId,

        @Schema(description = "Descrição da ocorrência", example = "Operação com velocidade reduzida devido à falha técnica na estação Sé.")
        String descricao,

        @Schema(description = "Severidade da ocorrência", example = "ALTA")
        String severidade,

        @Schema(description = "Data e hora de criação do alerta", example = "2026-09-27T17:30:00Z")
        Instant criadoEm
) {
}
