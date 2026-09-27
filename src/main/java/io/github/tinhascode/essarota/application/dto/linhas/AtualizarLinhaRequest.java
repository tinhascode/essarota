package io.github.tinhascode.essarota.application.dto.linhas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de uma linha de transporte")
public record AtualizarLinhaRequest(
        @Schema(description = "Novo nome identificador da linha", example = "Linha 1 - Azul (Jabaquara - Tucuruvi)")
        @NotBlank(message = "O nome da linha é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Novo tipo do transporte da linha", example = "METRO", allowableValues = {"METRO", "TREM", "ONIBUS"})
        @NotBlank(message = "O tipo da linha é obrigatório")
        @Size(min = 2, max = 30, message = "O tipo deve ter entre 2 e 30 caracteres")
        String tipo
) {
}
