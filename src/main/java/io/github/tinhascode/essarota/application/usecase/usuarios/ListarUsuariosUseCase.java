package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.application.mapper.UsuarioDtoMapper;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDtoMapper usuarioDtoMapper;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository, UsuarioDtoMapper usuarioDtoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> executar() {
        List<Usuario> usuarios = usuarioRepository.listarTodos();
        return usuarioDtoMapper.toResponseList(usuarios);
    }
}
