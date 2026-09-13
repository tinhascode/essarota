package io.github.tinhascode.essarota.application.usecase.auth;

import io.github.tinhascode.essarota.application.dto.auth.LoginRequest;
import io.github.tinhascode.essarota.application.dto.auth.LoginResponse;
import io.github.tinhascode.essarota.domain.exception.CredenciaisInvalidasException;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import io.github.tinhascode.essarota.domain.service.PasswordService;
import io.github.tinhascode.essarota.domain.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticarUsuarioUseCase {

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
        Usuario usuario = usuarioRepository.buscarPorEmail(request.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordService.validar(request.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = tokenService.gerarToken(usuario);
        return LoginResponse.bearer(token);
    }
}
