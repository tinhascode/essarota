package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.trajetoslinhas.AssociarLinhaTrajetoRequest;
import io.github.tinhascode.essarota.application.dto.trajetoslinhas.TrajetoLinhaResponse;
import io.github.tinhascode.essarota.application.usecase.trajetoslinhas.AssociarLinhaTrajetoUseCase;
import io.github.tinhascode.essarota.application.usecase.trajetoslinhas.ListarLinhasDoTrajetoUseCase;
import io.github.tinhascode.essarota.application.usecase.trajetoslinhas.RemoverLinhaDoTrajetoUseCase;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trajetos/{trajetoId}/linhas")
@Tag(name = "Trajetos - Linhas", description = "Endpoints para gerenciamento das linhas que compõem o percurso de um trajeto")
@SecurityRequirement(name = "Bearer Authentication")
public class TrajetoLinhaController {

    private static final Logger log = LoggerFactory.getLogger(TrajetoLinhaController.class);

    private final AssociarLinhaTrajetoUseCase associarLinhaTrajetoUseCase;
    private final ListarLinhasDoTrajetoUseCase listarLinhasDoTrajetoUseCase;
    private final RemoverLinhaDoTrajetoUseCase removerLinhaDoTrajetoUseCase;

    public TrajetoLinhaController(
            AssociarLinhaTrajetoUseCase associarLinhaTrajetoUseCase,
            ListarLinhasDoTrajetoUseCase listarLinhasDoTrajetoUseCase,
            RemoverLinhaDoTrajetoUseCase removerLinhaDoTrajetoUseCase
    ) {
        this.associarLinhaTrajetoUseCase = associarLinhaTrajetoUseCase;
        this.listarLinhasDoTrajetoUseCase = listarLinhasDoTrajetoUseCase;
        this.removerLinhaDoTrajetoUseCase = removerLinhaDoTrajetoUseCase;
    }

    @PostMapping
    @Operation(summary = "Associar linha ao trajeto", description = "Associa uma linha de transporte ao trajeto especificado, informando a ordem sequencial no percurso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Linha associada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrajetoLinhaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: o trajeto pertence a outro usuário"),
            @ApiResponse(responseCode = "404", description = "Trajeto ou Linha não encontrados")
    })
    public ResponseEntity<TrajetoLinhaResponse> associarLinha(
            @AuthenticationPrincipal Usuario usuarioAutenticado,
            @Parameter(description = "UUID do trajeto", example = "d94b0d74-c089-4e78-9e45-9858f9a26312") @PathVariable UUID trajetoId,
            @RequestBody @Valid AssociarLinhaTrajetoRequest request) {
        log.info("Usuário ID '{}' associando linha ID '{}' ao trajeto ID '{}'",
                usuarioAutenticado.getId(), request.linhaId(), trajetoId);
        TrajetoLinhaResponse response = associarLinhaTrajetoUseCase.executar(trajetoId, usuarioAutenticado.getId(), request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{linhaId}")
                .buildAndExpand(response.linhaId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar linhas do trajeto", description = "Retorna todas as linhas associadas ao trajeto informado, ordenadas pela sequência do trajeto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linhas do trajeto retornadas com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrajetoLinhaResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: o trajeto pertence a outro usuário"),
            @ApiResponse(responseCode = "404", description = "Trajeto não encontrado")
    })
    public ResponseEntity<List<TrajetoLinhaResponse>> listarLinhas(
            @AuthenticationPrincipal Usuario usuarioAutenticado,
            @Parameter(description = "UUID do trajeto", example = "d94b0d74-c089-4e78-9e45-9858f9a26312") @PathVariable UUID trajetoId) {
        log.info("Usuário ID '{}' listando linhas do trajeto ID '{}'", usuarioAutenticado.getId(), trajetoId);
        return ResponseEntity.ok(listarLinhasDoTrajetoUseCase.executar(trajetoId, usuarioAutenticado.getId()));
    }

    @DeleteMapping("/{linhaId}")
    @Operation(summary = "Desassociar linha do trajeto", description = "Remove a associação de uma linha específica do trajeto informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Linha desassociada com sucesso (sem corpo de retorno)"),
            @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: o trajeto pertence a outro usuário"),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada")
    })
    public ResponseEntity<Void> removerLinha(
            @AuthenticationPrincipal Usuario usuarioAutenticado,
            @Parameter(description = "UUID do trajeto", example = "d94b0d74-c089-4e78-9e45-9858f9a26312") @PathVariable UUID trajetoId,
            @Parameter(description = "UUID da linha", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID linhaId) {
        log.info("Usuário ID '{}' removendo linha ID '{}' do trajeto ID '{}'",
                usuarioAutenticado.getId(), linhaId, trajetoId);
        removerLinhaDoTrajetoUseCase.executar(trajetoId, linhaId, usuarioAutenticado.getId());
        return ResponseEntity.noContent().build();
    }
}
