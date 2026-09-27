package io.github.tinhascode.essarota.application.dto.trajetoslinhas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para associar uma linha a um trajeto")
public record AssociarLinhaTrajetoRequest(
        @Schema(description = "Identificador da linha a ser associada (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "O identificador da linha é obrigatório")
        UUID linhaId,

        @Schema(description = "Ordem cronológica ou sequencial da linha no trajeto", example = "1")
        @Min(value = 1, message = "A ordem deve ser maior ou igual a 1")
        Integer ordem
) {
}
