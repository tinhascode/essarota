package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.domain.exception.UsuarioNaoEncontradoException;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeletarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public DeletarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void executar(UUID id) {
        if (!usuarioRepository.existePorId(id)) {
            throw new UsuarioNaoEncontradoException(id);
        }
        usuarioRepository.deletarPorId(id);
    }
}
