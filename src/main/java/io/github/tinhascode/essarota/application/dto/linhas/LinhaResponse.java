package io.github.tinhascode.essarota.application.dto.linhas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Representação detalhada de uma linha de transporte")
public record LinhaResponse(
        @Schema(description = "Identificador único da linha (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Nome da linha", example = "Linha 1 - Azul")
        String nome,

        @Schema(description = "Tipo do transporte", example = "METRO")
        String tipo
) {
}
