package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.notificacoes.CriarNotificacaoRequest;
import io.github.tinhascode.essarota.application.dto.notificacoes.NotificacaoResponse;
import io.github.tinhascode.essarota.application.usecase.notificacoes.BuscarNotificacaoPorIdUseCase;
import io.github.tinhascode.essarota.application.usecase.notificacoes.CriarNotificacaoUseCase;
import io.github.tinhascode.essarota.application.usecase.notificacoes.ListarNotificacoesPorUsuarioUseCase;
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
@RequestMapping("/api/v1/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para registro e consulta de histórico de notificações enviadas")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificacaoController {

        private static final Logger log = LoggerFactory.getLogger(NotificacaoController.class);

        private final CriarNotificacaoUseCase criarNotificacaoUseCase;
        private final BuscarNotificacaoPorIdUseCase buscarNotificacaoPorIdUseCase;
        private final ListarNotificacoesPorUsuarioUseCase listarNotificacoesPorUsuarioUseCase;

        public NotificacaoController(
                        CriarNotificacaoUseCase criarNotificacaoUseCase,
                        BuscarNotificacaoPorIdUseCase buscarNotificacaoPorIdUseCase,
                        ListarNotificacoesPorUsuarioUseCase listarNotificacoesPorUsuarioUseCase) {
                this.criarNotificacaoUseCase = criarNotificacaoUseCase;
                this.buscarNotificacaoPorIdUseCase = buscarNotificacaoPorIdUseCase;
                this.listarNotificacoesPorUsuarioUseCase = listarNotificacoesPorUsuarioUseCase;
        }

        @PostMapping
        @Operation(summary = "Registrar notificação", description = "Registra um evento de notificação disparado para um usuário por um canal específico (ex: WHATSAPP, PUSH).")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Notificação registrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificacaoResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Usuário ou Alerta não encontrado")
        })
        public ResponseEntity<NotificacaoResponse> criar(@RequestBody @Valid CriarNotificacaoRequest request) {
                log.info("Registrando notificação para usuário ID '{}', alerta ID '{}' via '{}'",
                                request.usuarioId(), request.alertaId(), request.canal());
                NotificacaoResponse response = criarNotificacaoUseCase.executar(request);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();
                return ResponseEntity.created(location).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar notificação por ID", description = "Recupera uma notificação específica pelo seu UUID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Notificação encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificacaoResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
        })
        public ResponseEntity<NotificacaoResponse> buscarPorId(
                        @Parameter(description = "UUID da notificação", example = "b11a5b82-8bfb-4dc7-a1cb-9e0a05b38dcb") @PathVariable UUID id) {
                log.info("Buscando notificação ID '{}'", id);
                return ResponseEntity.ok(buscarNotificacaoPorIdUseCase.executar(id));
        }

        @GetMapping("/me")
        @Operation(summary = "Listar notificações do usuário", description = "Retorna o histórico de notificações recebidas pelo usuário autenticado via JWT.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotificacaoResponse.class)))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado")
        })
        public ResponseEntity<List<NotificacaoResponse>> listarMinhasNotificacoes(
                        @AuthenticationPrincipal Usuario usuarioAutenticado) {
                log.info("Usuário ID '{}' buscando seu histórico de notificações", usuarioAutenticado.getId());
                return ResponseEntity.ok(listarNotificacoesPorUsuarioUseCase.executar(usuarioAutenticado.getId()));
        }
}
