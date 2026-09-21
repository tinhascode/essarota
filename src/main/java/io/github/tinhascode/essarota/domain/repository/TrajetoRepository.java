package io.github.tinhascode.essarota.domain.repository;

import io.github.tinhascode.essarota.domain.model.Trajeto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrajetoRepository {

    Trajeto salvar(Trajeto trajeto);

    Optional<Trajeto> buscarPorId(UUID id);

    List<Trajeto> listarPorUsuarioId(UUID usuarioId);

    void deletarPorId(UUID id);

    boolean existePorId(UUID id);

    boolean pertenceAoUsuario(UUID trajetoId, UUID usuarioId);
}
