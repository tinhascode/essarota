package io.github.tinhascode.essarota.domain.repository;

import io.github.tinhascode.essarota.domain.model.Notificacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificacaoRepository {

    Notificacao salvar(Notificacao notificacao);

    Optional<Notificacao> buscarPorId(UUID id);

    List<Notificacao> listarPorUsuarioId(UUID usuarioId);

    void deletarPorId(UUID id);

    boolean existePorId(UUID id);
}
