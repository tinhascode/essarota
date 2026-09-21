package io.github.tinhascode.essarota.infrastructure.web.controller;

import io.github.tinhascode.essarota.application.dto.auth.LoginRequest;
import io.github.tinhascode.essarota.application.dto.auth.LoginResponse;
import io.github.tinhascode.essarota.application.usecase.auth.AutenticarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e obtenção de token JWT")
public class AuthController {

        private static final Logger log = LoggerFactory.getLogger(AuthController.class);

        private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

        public AuthController(AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
                this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        }

        @PostMapping("/login")
        @SecurityRequirements
        @Operation(summary = "Efetuar login", description = "Autentica um usuário existente a partir de e-mail e senha, retornando o token JWT para autorização das demais rotas.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Autenticado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos (ex: e-mail em formato incorreto ou campos vazios)"),
                        @ApiResponse(responseCode = "401", description = "Credenciais inválidas (e-mail ou senha incorretos)")
        })
        public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
                log.info("Tentativa de login recebida para o email: {}", request.email());
                LoginResponse response = autenticarUsuarioUseCase.executar(request);
                log.info("Login realizado com sucesso para o email: {}. Token JWT gerado.", request.email());
                return ResponseEntity.ok(response);
        }
}
