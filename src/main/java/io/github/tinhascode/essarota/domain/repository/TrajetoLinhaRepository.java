package io.github.tinhascode.essarota.domain.repository;

import io.github.tinhascode.essarota.domain.model.TrajetoLinha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrajetoLinhaRepository {

    TrajetoLinha salvar(TrajetoLinha trajetoLinha);

    Optional<TrajetoLinha> buscarPorId(UUID id);

    List<TrajetoLinha> listarPorTrajetoId(UUID trajetoId);

    void deletarPorId(UUID id);

    void deletarPorTrajetoIdELinhaId(UUID trajetoId, UUID linhaId);

    boolean existePorTrajetoIdELinhaId(UUID trajetoId, UUID linhaId);

    Optional<TrajetoLinha> buscarPorTrajetoIdELinhaId(UUID trajetoId, UUID linhaId);
}
