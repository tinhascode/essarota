package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.application.dto.usuarios.AtualizarUsuarioRequest;
import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.application.mapper.UsuarioDtoMapper;
import io.github.tinhascode.essarota.domain.exception.EmailJaCadastradoException;
import io.github.tinhascode.essarota.domain.exception.UsuarioNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import io.github.tinhascode.essarota.domain.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AtualizarUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(AtualizarUsuarioUseCase.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDtoMapper usuarioDtoMapper;
    private final PasswordService passwordService;

    public AtualizarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            UsuarioDtoMapper usuarioDtoMapper,
            PasswordService passwordService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
        this.passwordService = passwordService;
    }

    @Transactional
    public UsuarioResponse executar(UUID id, AtualizarUsuarioRequest request) {
        log.debug("Executando AtualizarUsuarioUseCase para ID: {}", id);
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar: usuário não encontrado com ID: {}", id);
                    return new UsuarioNaoEncontradoException(id);
                });

        String emailNormalizado = request.email().trim().toLowerCase();
        if (!usuario.getEmail().equalsIgnoreCase(emailNormalizado)) {
            Optional<Usuario> usuarioComMesmoEmail = usuarioRepository.buscarPorEmail(emailNormalizado);
            if (usuarioComMesmoEmail.isPresent() && !usuarioComMesmoEmail.get().getId().equals(id)) {
                log.warn("Falha ao atualizar: o e-mail '{}' já está em uso por outro usuário", request.email());
                throw new EmailJaCadastradoException(request.email());
            }
        }

        usuario.atualizar(request.nome(), request.email(), request.telefoneWhatsapp(), request.deviceToken());

        if (request.senha() != null && !request.senha().isBlank()) {
            log.debug("Nova senha fornecida para o usuário ID: {}. Criptografando senha.", id);
            usuario.atualizarSenha(passwordService.codificar(request.senha()));
        }

        Usuario atualizado = usuarioRepository.salvar(usuario);
        log.info("Usuário ID: {} atualizado com sucesso no repositório", id);
        return usuarioDtoMapper.toResponse(atualizado);
    }
}
