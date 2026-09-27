package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.linhas.AtualizarLinhaRequest;
import io.github.tinhascode.essarota.application.dto.linhas.CriarLinhaRequest;
import io.github.tinhascode.essarota.application.dto.linhas.LinhaResponse;
import io.github.tinhascode.essarota.application.usecase.linhas.AtualizarLinhaUseCase;
import io.github.tinhascode.essarota.application.usecase.linhas.BuscarLinhaPorIdUseCase;
import io.github.tinhascode.essarota.application.usecase.linhas.CriarLinhaUseCase;
import io.github.tinhascode.essarota.application.usecase.linhas.DeletarLinhaUseCase;
import io.github.tinhascode.essarota.application.usecase.linhas.ListarLinhasUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/linhas")
@Tag(name = "Linhas", description = "Endpoints para gerenciamento de linhas de transporte público")
@SecurityRequirement(name = "Bearer Authentication")
public class LinhaController {

        private static final Logger log = LoggerFactory.getLogger(LinhaController.class);

        private final CriarLinhaUseCase criarLinhaUseCase;
        private final BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase;
        private final ListarLinhasUseCase listarLinhasUseCase;
        private final AtualizarLinhaUseCase atualizarLinhaUseCase;
        private final DeletarLinhaUseCase deletarLinhaUseCase;

        public LinhaController(
                        CriarLinhaUseCase criarLinhaUseCase,
                        BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase,
                        ListarLinhasUseCase listarLinhasUseCase,
                        AtualizarLinhaUseCase atualizarLinhaUseCase,
                        DeletarLinhaUseCase deletarLinhaUseCase) {
                this.criarLinhaUseCase = criarLinhaUseCase;
                this.buscarLinhaPorIdUseCase = buscarLinhaPorIdUseCase;
                this.listarLinhasUseCase = listarLinhasUseCase;
                this.atualizarLinhaUseCase = atualizarLinhaUseCase;
                this.deletarLinhaUseCase = deletarLinhaUseCase;
        }

        @PostMapping
        @Operation(summary = "Cadastrar linha", description = "Cadastra uma nova linha de transporte público no sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Linha cadastrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinhaResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Erro de validação nos campos informados"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado")
        })
        public ResponseEntity<LinhaResponse> criar(@RequestBody @Valid CriarLinhaRequest request) {
                log.info("Requisição para criar linha '{}' do tipo '{}'", request.nome(), request.tipo());
                LinhaResponse response = criarLinhaUseCase.executar(request);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();
                return ResponseEntity.created(location).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar linha por ID", description = "Busca uma linha de transporte específica pelo seu UUID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Linha recuperada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinhaResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Linha não encontrada")
        })
        public ResponseEntity<LinhaResponse> buscarPorId(
                        @Parameter(description = "UUID da linha", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
                log.info("Buscando linha ID '{}'", id);
                return ResponseEntity.ok(buscarLinhaPorIdUseCase.executar(id));
        }

        @GetMapping
        @Operation(summary = "Listar todas as linhas", description = "Retorna a lista completa de linhas de transporte cadastradas.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de linhas retornada com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LinhaResponse.class)))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado")
        })
        public ResponseEntity<List<LinhaResponse>> listarTodas() {
                log.info("Listando todas as linhas de transporte");
                return ResponseEntity.ok(listarLinhasUseCase.executar());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar linha", description = "Atualiza os dados cadastrais de uma linha existente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Linha atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinhaResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Linha não encontrada")
        })
        public ResponseEntity<LinhaResponse> atualizar(
                        @Parameter(description = "UUID da linha", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id,
                        @RequestBody @Valid AtualizarLinhaRequest request) {
                log.info("Atualizando linha ID '{}'", id);
                LinhaResponse response = atualizarLinhaUseCase.executar(id, request);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Excluir linha", description = "Exclui uma linha de transporte a partir de seu UUID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Linha excluída com sucesso (sem corpo)"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Linha não encontrada")
        })
        public ResponseEntity<Void> deletar(
                        @Parameter(description = "UUID da linha", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
                log.info("Excluindo linha ID '{}'", id);
                deletarLinhaUseCase.executar(id);
                return ResponseEntity.noContent().build();
        }
}
