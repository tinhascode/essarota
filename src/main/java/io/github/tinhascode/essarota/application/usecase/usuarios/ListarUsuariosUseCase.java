package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.application.mapper.UsuarioDtoMapper;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarUsuariosUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarUsuariosUseCase.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDtoMapper usuarioDtoMapper;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository, UsuarioDtoMapper usuarioDtoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> executar() {
        log.debug("Executando ListarUsuariosUseCase");
        List<Usuario> usuarios = usuarioRepository.listarTodos();
        log.debug("Total de usuários recuperados: {}", usuarios.size());
        return usuarioDtoMapper.toResponseList(usuarios);
    }
}
