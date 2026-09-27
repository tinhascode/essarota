package io.github.tinhascode.essarota.application.dto.alertas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Dados para cadastro de um novo alerta em uma linha")
public record CriarAlertaRequest(
        @Schema(description = "Identificador da linha que sofreu o evento (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "O ID da linha é obrigatório")
        UUID linhaId,

        @Schema(description = "Descrição detalhada do problema na linha", example = "Operação com velocidade reduzida devido à falha técnica na estação Sé.")
        @NotBlank(message = "A descrição do alerta é obrigatória")
        @Size(min = 5, max = 500, message = "A descrição deve conter entre 5 e 500 caracteres")
        String descricao,

        @Schema(description = "Severidade do alerta", example = "ALTA", allowableValues = {"BAIXA", "MEDIA", "ALTA", "GRAVE"})
        @NotBlank(message = "A severidade é obrigatória")
        @Size(min = 2, max = 30, message = "A severidade deve conter entre 2 e 30 caracteres")
        String severidade
) {
}
