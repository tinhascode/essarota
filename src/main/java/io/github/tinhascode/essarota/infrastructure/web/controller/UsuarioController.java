package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.usuarios.AtualizarUsuarioRequest;
import io.github.tinhascode.essarota.application.dto.usuarios.CriarUsuarioRequest;
import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.application.usecase.usuarios.AtualizarUsuarioUseCase;
import io.github.tinhascode.essarota.application.usecase.usuarios.BuscarUsuarioPorIdUseCase;
import io.github.tinhascode.essarota.application.usecase.usuarios.CriarUsuarioUseCase;
import io.github.tinhascode.essarota.application.usecase.usuarios.DeletarUsuarioUseCase;
import io.github.tinhascode.essarota.application.usecase.usuarios.ListarUsuariosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento e cadastro de usuários")
public class UsuarioController {

        private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

        private final CriarUsuarioUseCase criarUsuarioUseCase;
        private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
        private final ListarUsuariosUseCase listarUsuariosUseCase;
        private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
        private final DeletarUsuarioUseCase deletarUsuarioUseCase;

        public UsuarioController(
                        CriarUsuarioUseCase criarUsuarioUseCase,
                        BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
                        ListarUsuariosUseCase listarUsuariosUseCase,
                        AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                        DeletarUsuarioUseCase deletarUsuarioUseCase) {
                this.criarUsuarioUseCase = criarUsuarioUseCase;
                this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
                this.listarUsuariosUseCase = listarUsuariosUseCase;
                this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
                this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        }

        @PostMapping
        @SecurityRequirements
        @Operation(summary = "Criar um novo usuário (Público)", description = "Cadastra um novo usuário no sistema com senha criptografada via BCrypt. Não requer token JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Erro de validação nos campos informados"),
                        @ApiResponse(responseCode = "409", description = "Conflito: já existe usuário cadastrado com este e-mail")
        })
        public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid CriarUsuarioRequest request) {
                log.info("Recebida requisição para cadastrar usuário com email='{}' e nome='{}'", request.email(), request.nome());
                UsuarioResponse response = criarUsuarioUseCase.executar(request);
                log.info("Usuário cadastrado com sucesso. ID gerado: {}", response.id());
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();
                return ResponseEntity.created(location).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar usuário por ID", description = "Recupera os detalhes de um usuário pelo seu identificador único UUID. Requer token JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        })
        public ResponseEntity<UsuarioResponse> buscarPorId(
                        @Parameter(description = "UUID do usuário", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185") @PathVariable UUID id) {
                log.info("Buscando dados do usuário por ID: {}", id);
                return ResponseEntity.ok(buscarUsuarioPorIdUseCase.executar(id));
        }

        @GetMapping
        @Operation(summary = "Listar todos os usuários", description = "Retorna a listagem de todos os usuários registrados. Requer token JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado")
        })
        public ResponseEntity<List<UsuarioResponse>> listarTodos() {
                log.info("Listando todos os usuários cadastrados");
                return ResponseEntity.ok(listarUsuariosUseCase.executar());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar usuário existente", description = "Atualiza os dados de cadastro de um usuário existente. Se informado o campo senha, ela será criptografada e atualizada. Requer token JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos no corpo da requisição"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID fornecido"),
                        @ApiResponse(responseCode = "409", description = "Conflito: outro usuário já está usando o novo e-mail informado")
        })
        public ResponseEntity<UsuarioResponse> atualizar(
                        @Parameter(description = "UUID do usuário a ser atualizado", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185") @PathVariable UUID id,
                        @RequestBody @Valid AtualizarUsuarioRequest request) {
                log.info("Recebida requisição para atualizar usuário ID: {}", id);
                UsuarioResponse response = atualizarUsuarioUseCase.executar(id, request);
                log.info("Usuário ID: {} atualizado com sucesso", id);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema com base no seu UUID. Requer token JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso (sem corpo de retorno)"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado para exclusão")
        })
        public ResponseEntity<Void> deletar(
                        @Parameter(description = "UUID do usuário a ser excluído", example = "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185") @PathVariable UUID id) {
                log.info("Recebida requisição para deletar usuário ID: {}", id);
                deletarUsuarioUseCase.executar(id);
                log.info("Usuário ID: {} deletado com sucesso", id);
                return ResponseEntity.noContent().build();
        }
}
