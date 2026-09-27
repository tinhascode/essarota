package io.github.tinhascode.essarota.infrastructure.persistence.repository;

import io.github.tinhascode.essarota.infrastructure.persistence.entity.NotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataNotificacaoRepository extends JpaRepository<NotificacaoEntity, UUID> {

    List<NotificacaoEntity> findByUsuarioIdOrderByEnviadoEmDesc(UUID usuarioId);
}
