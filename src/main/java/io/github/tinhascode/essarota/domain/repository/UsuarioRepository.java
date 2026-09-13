package io.github.tinhascode.essarota.domain.repository;

import io.github.tinhascode.essarota.domain.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarTodos();

    void deletarPorId(UUID id);

    boolean existePorId(UUID id);

    boolean existePorEmail(String email);
}
