package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.application.mapper.UsuarioDtoMapper;
import io.github.tinhascode.essarota.domain.exception.UsuarioNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarUsuarioPorIdUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDtoMapper usuarioDtoMapper;

    public BuscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository, UsuarioDtoMapper usuarioDtoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
    }

    @Transactional(readOnly = true)
    public UsuarioResponse executar(UUID id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return usuarioDtoMapper.toResponse(usuario);
    }
}
