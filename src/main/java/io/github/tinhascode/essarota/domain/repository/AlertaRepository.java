package io.github.tinhascode.essarota.domain.repository;

import io.github.tinhascode.essarota.domain.model.Alerta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertaRepository {

    Alerta salvar(Alerta alerta);

    Optional<Alerta> buscarPorId(UUID id);

    List<Alerta> listarPorLinhaId(UUID linhaId);

    List<Alerta> listarTodos();

    void deletarPorId(UUID id);

    boolean existePorId(UUID id);
}
