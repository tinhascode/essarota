package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.alertas.AlertaResponse;
import io.github.tinhascode.essarota.application.dto.alertas.CriarAlertaRequest;
import io.github.tinhascode.essarota.application.usecase.alertas.BuscarAlertaPorIdUseCase;
import io.github.tinhascode.essarota.application.usecase.alertas.CriarAlertaUseCase;
import io.github.tinhascode.essarota.application.usecase.alertas.DeletarAlertaUseCase;
import io.github.tinhascode.essarota.application.usecase.alertas.ListarAlertasPorLinhaUseCase;
import io.github.tinhascode.essarota.application.usecase.alertas.ListarAlertasUseCase;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alertas")
@Tag(name = "Alertas", description = "Endpoints para registro, consulta e gerenciamento de alertas operacionais nas linhas")
@SecurityRequirement(name = "Bearer Authentication")
public class AlertaController {

        private static final Logger log = LoggerFactory.getLogger(AlertaController.class);

        private final CriarAlertaUseCase criarAlertaUseCase;
        private final BuscarAlertaPorIdUseCase buscarAlertaPorIdUseCase;
        private final ListarAlertasUseCase listarAlertasUseCase;
        private final ListarAlertasPorLinhaUseCase listarAlertasPorLinhaUseCase;
        private final DeletarAlertaUseCase deletarAlertaUseCase;

        public AlertaController(
                        CriarAlertaUseCase criarAlertaUseCase,
                        BuscarAlertaPorIdUseCase buscarAlertaPorIdUseCase,
                        ListarAlertasUseCase listarAlertasUseCase,
                        ListarAlertasPorLinhaUseCase listarAlertasPorLinhaUseCase,
                        DeletarAlertaUseCase deletarAlertaUseCase) {
                this.criarAlertaUseCase = criarAlertaUseCase;
                this.buscarAlertaPorIdUseCase = buscarAlertaPorIdUseCase;
                this.listarAlertasUseCase = listarAlertasUseCase;
                this.listarAlertasPorLinhaUseCase = listarAlertasPorLinhaUseCase;
                this.deletarAlertaUseCase = deletarAlertaUseCase;
        }

        @PostMapping
        @Operation(summary = "Criar alerta", description = "Registra uma nova ocorrência operacional ou problema técnico para uma linha.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Alerta registrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Linha informada não encontrada")
        })
        public ResponseEntity<AlertaResponse> criar(@RequestBody @Valid CriarAlertaRequest request) {
                log.info("Registrando alerta para linha ID '{}' com severidade '{}'", request.linhaId(),
                                request.severidade());
                AlertaResponse response = criarAlertaUseCase.executar(request);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();
                return ResponseEntity.created(location).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar alerta por ID", description = "Recupera os detalhes de um alerta a partir de seu UUID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Alerta encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertaResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
        })
        public ResponseEntity<AlertaResponse> buscarPorId(
                        @Parameter(description = "UUID do alerta", example = "8a32d1ef-1980-4965-a82f-8557b4260efb") @PathVariable UUID id) {
                log.info("Buscando alerta ID '{}'", id);
                return ResponseEntity.ok(buscarAlertaPorIdUseCase.executar(id));
        }

        @GetMapping
        @Operation(summary = "Listar alertas", description = "Retorna todos os alertas registrados ou filtra pelos alertas de uma linha específica via query param 'linhaId'.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de alertas retornada com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AlertaResponse.class)))),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Linha informada no filtro não encontrada")
        })
        public ResponseEntity<List<AlertaResponse>> listar(
                        @Parameter(description = "UUID da linha para filtrar (opcional)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @RequestParam(required = false) UUID linhaId) {
                if (linhaId != null) {
                        log.info("Listando alertas da linha ID '{}'", linhaId);
                        return ResponseEntity.ok(listarAlertasPorLinhaUseCase.executar(linhaId));
                }
                log.info("Listando todos os alertas");
                return ResponseEntity.ok(listarAlertasUseCase.executar());
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Excluir alerta", description = "Exclui um registro de alerta a partir de seu UUID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Alerta excluído com sucesso (sem corpo)"),
                        @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido/expirado"),
                        @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
        })
        public ResponseEntity<Void> deletar(
                        @Parameter(description = "UUID do alerta", example = "8a32d1ef-1980-4965-a82f-8557b4260efb") @PathVariable UUID id) {
                log.info("Excluindo alerta ID '{}'", id);
                deletarAlertaUseCase.executar(id);
                return ResponseEntity.noContent().build();
        }
}
