package io.github.tinhascode.essarota.domain.repository;

import io.github.tinhascode.essarota.domain.model.Linha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinhaRepository {

    Linha salvar(Linha linha);

    Optional<Linha> buscarPorId(UUID id);

    List<Linha> listarTodas();

    void deletarPorId(UUID id);

    boolean existePorId(UUID id);
}
