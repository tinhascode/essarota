package io.github.tinhascode.essarota.application.usecase.auth;

import io.github.tinhascode.essarota.application.dto.auth.LoginRequest;
import io.github.tinhascode.essarota.application.dto.auth.LoginResponse;
import io.github.tinhascode.essarota.domain.exception.CredenciaisInvalidasException;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import io.github.tinhascode.essarota.domain.service.PasswordService;
import io.github.tinhascode.essarota.domain.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticarUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(AutenticarUsuarioUseCase.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;

    public AutenticarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            PasswordService passwordService,
            TokenService tokenService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public LoginResponse executar(LoginRequest request) {
        log.debug("Iniciando processo de autenticação para e-mail: {}", request.email());

        Usuario usuario = usuarioRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Falha de autenticação: usuário com e-mail '{}' não encontrado", request.email());
                    return new CredenciaisInvalidasException();
                });

        if (!passwordService.validar(request.senha(), usuario.getSenha())) {
            log.warn("Falha de autenticação: senha incorreta para e-mail '{}'", request.email());
            throw new CredenciaisInvalidasException();
        }

        String token = tokenService.gerarToken(usuario);
        log.info("Usuário '{}' (ID: {}) autenticado com sucesso", usuario.getEmail(), usuario.getId());
        return LoginResponse.bearer(token);
    }
}
