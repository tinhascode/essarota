package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.domain.exception.UsuarioNaoEncontradoException;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeletarUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeletarUsuarioUseCase.class);

    private final UsuarioRepository usuarioRepository;

    public DeletarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void executar(UUID id) {
        log.debug("Executando DeletarUsuarioUseCase para ID: {}", id);
        if (!usuarioRepository.existePorId(id)) {
            log.warn("Falha ao deletar: usuário com ID {} não encontrado", id);
            throw new UsuarioNaoEncontradoException(id);
        }
        usuarioRepository.deletarPorId(id);
        log.info("Usuário com ID {} removido com sucesso do repositório", id);
    }
}
