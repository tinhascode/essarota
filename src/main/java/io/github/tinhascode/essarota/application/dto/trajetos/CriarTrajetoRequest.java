package io.github.tinhascode.essarota.application.dto.trajetos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de um novo trajeto")
public record CriarTrajetoRequest(
        @Schema(description = "Ponto de partida / origem do trajeto", example = "Terminal Metrô Santana, São Paulo - SP")
        @NotBlank(message = "A origem é obrigatória")
        @Size(min = 2, max = 150, message = "A origem deve ter entre 2 e 150 caracteres")
        String origem,

        @Schema(description = "Ponto de chegada / destino do trajeto", example = "Avenida Paulista, 1000, São Paulo - SP")
        @NotBlank(message = "O destino é obrigatório")
        @Size(min = 2, max = 150, message = "O destino deve ter entre 2 e 150 caracteres")
        String destino,

        @Schema(description = "Tempo estimado em minutos (opcional; se omitido, será calculado automaticamente)", example = "45")
        @Min(value = 1, message = "O tempo estimado em minutos deve ser no mínimo 1")
        Integer tempoEstimadoMinutos
) {
}
