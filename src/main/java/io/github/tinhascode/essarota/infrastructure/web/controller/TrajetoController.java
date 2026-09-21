package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.trajetos.AtualizarTrajetoRequest;
import io.github.tinhascode.essarota.application.dto.trajetos.CriarTrajetoRequest;
import io.github.tinhascode.essarota.application.dto.trajetos.TrajetoResponse;
import io.github.tinhascode.essarota.application.usecase.trajetos.AtualizarTrajetoUseCase;
import io.github.tinhascode.essarota.application.usecase.trajetos.BuscarTrajetoPorIdUseCase;
import io.github.tinhascode.essarota.application.usecase.trajetos.CriarTrajetoUseCase;
import io.github.tinhascode.essarota.application.usecase.trajetos.DeletarTrajetoUseCase;
import io.github.tinhascode.essarota.application.usecase.trajetos.ListarTrajetosPorUsuarioUseCase;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trajetos")
@Tag(name = "Trajetos", description = "Endpoints para gerenciamento de trajetos do usuário autenticado")
@SecurityRequirement(name = "Bearer Authentication")
public class TrajetoController {

        private static final Logger log = LoggerFactory.getLogger(TrajetoController.class);

        private final CriarTrajetoUseCase criarTrajetoUseCase;
        private final BuscarTrajetoPorIdUseCase buscarTrajetoPorIdUseCase;
        private final ListarTrajetosPorUsuarioUseCase listarTrajetosPorUsuarioUseCase;
        private final AtualizarTrajetoUseCase atualizarTrajetoUseCase;
        private final DeletarTrajetoUseCase deletarTrajetoUseCase;

        public TrajetoController(
                        CriarTrajetoUseCase criarTrajetoUseCase,
                        BuscarTrajetoPorIdUseCase buscarTrajetoPorIdUseCase,
                        ListarTrajetosPorUsuarioUseCase listarTrajetosPorUsuarioUseCase,
                        AtualizarTrajetoUseCase atualizarTrajetoUseCase,
                        DeletarTrajetoUseCase deletarTrajetoUseCase) {
                this.criarTrajetoUseCase = criarTrajetoUseCase;
                this.buscarTrajetoPorIdUseCase = buscarTrajetoPorIdUseCase;
                this.listarTrajetosPorUsuarioUseCase = listarTrajetosPorUsuarioUseCase;
                this.atualizarTrajetoUseCase = atualizarTrajetoUseCase;
                this.deletarTrajetoUseCase = deletarTrajetoUseCase;
        }

        @PostMapping
        @Operation(summary = "Cadastrar trajeto", description = "Cadastra um novo trajeto para o usuário autenticado via JWT. O tempo estimado é opcional e calculado automaticamente se omitido.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Trajeto cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrajetoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Erro de validação nos campos informados"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado")
        })
        public ResponseEntity<TrajetoResponse> criar(
                        @AuthenticationPrincipal Usuario usuarioAutenticado,
                        @RequestBody @Valid CriarTrajetoRequest request) {
                log.info("Usuário ID '{}' criando trajeto de '{}' para '{}'", usuarioAutenticado.getId(), request.origem(), request.destino());
                TrajetoResponse response = criarTrajetoUseCase.executar(usuarioAutenticado.getId(), request);
                log.info("Trajeto cadastrado com sucesso. ID: {}, tempo estimado: {} min", response.id(), response.tempoEstimadoMinutos());
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();
                return ResponseEntity.created(location).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar trajeto por ID", description = "Busca um trajeto específico pelo seu UUID. Apenas o proprietário do trajeto tem permissão de acesso.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Trajeto recuperado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrajetoResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "403", description = "Acesso negado: o trajeto pertence a outro usuário"),
                        @ApiResponse(responseCode = "404", description = "Trajeto não encontrado")
        })
        public ResponseEntity<TrajetoResponse> buscarPorId(
                        @AuthenticationPrincipal Usuario usuarioAutenticado,
                        @Parameter(description = "UUID do trajeto", example = "d94b0d74-c089-4e78-9e45-9858f9a26312") @PathVariable UUID id) {
                log.info("Usuário ID '{}' buscando trajeto ID '{}'", usuarioAutenticado.getId(), id);
                return ResponseEntity.ok(buscarTrajetoPorIdUseCase.executar(id, usuarioAutenticado.getId()));
        }

        @GetMapping
        @Operation(summary = "Listar trajetos do usuário", description = "Retorna todos os trajetos cadastrados pelo usuário autenticado via token JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de trajetos retornada com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrajetoResponse.class)))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado")
        })
        public ResponseEntity<List<TrajetoResponse>> listarTodos(
                        @AuthenticationPrincipal Usuario usuarioAutenticado) {
                log.info("Usuário ID '{}' listando seus trajetos", usuarioAutenticado.getId());
                return ResponseEntity.ok(listarTrajetosPorUsuarioUseCase.executar(usuarioAutenticado.getId()));
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar trajeto", description = "Atualiza os dados de um trajeto existente. Apenas o proprietário do trajeto tem permissão para alterá-lo.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Trajeto atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrajetoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "403", description = "Acesso negado: o trajeto pertence a outro usuário"),
                        @ApiResponse(responseCode = "404", description = "Trajeto não encontrado")
        })
        public ResponseEntity<TrajetoResponse> atualizar(
                        @AuthenticationPrincipal Usuario usuarioAutenticado,
                        @Parameter(description = "UUID do trajeto", example = "d94b0d74-c089-4e78-9e45-9858f9a26312") @PathVariable UUID id,
                        @RequestBody @Valid AtualizarTrajetoRequest request) {
                log.info("Usuário ID '{}' atualizando trajeto ID '{}'", usuarioAutenticado.getId(), id);
                TrajetoResponse response = atualizarTrajetoUseCase.executar(id, usuarioAutenticado.getId(), request);
                log.info("Trajeto ID '{}' atualizado com sucesso", id);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Excluir trajeto", description = "Exclui um trajeto do usuário autenticado a partir do UUID. Apenas o proprietário tem permissão.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Trajeto excluído com sucesso (sem corpo de retorno)"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "403", description = "Acesso negado: o trajeto pertence a outro usuário"),
                        @ApiResponse(responseCode = "404", description = "Trajeto não encontrado")
        })
        public ResponseEntity<Void> deletar(
                        @AuthenticationPrincipal Usuario usuarioAutenticado,
                        @Parameter(description = "UUID do trajeto", example = "d94b0d74-c089-4e78-9e45-9858f9a26312") @PathVariable UUID id) {
                log.info("Usuário ID '{}' solicitando exclusão do trajeto ID '{}'", usuarioAutenticado.getId(), id);
                deletarTrajetoUseCase.executar(id, usuarioAutenticado.getId());
                log.info("Trajeto ID '{}' excluído com sucesso", id);
                return ResponseEntity.noContent().build();
        }
}
