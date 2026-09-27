package io.github.tinhascode.essarota.application.dto.trajetoslinhas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Representação detalhada da associação entre um trajeto e uma linha")
public record TrajetoLinhaResponse(
        @Schema(description = "Identificador único da associação (UUID)", example = "7ca194a2-1132-4d2b-9c3f-c3866380a976")
        UUID id,

        @Schema(description = "Identificador do trajeto (UUID)", example = "d94b0d74-c089-4e78-9e45-9858f9a26312")
        UUID trajetoId,

        @Schema(description = "Identificador da linha (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID linhaId,

        @Schema(description = "Ordem da linha no percurso do trajeto", example = "1")
        Integer ordem
) {
}
