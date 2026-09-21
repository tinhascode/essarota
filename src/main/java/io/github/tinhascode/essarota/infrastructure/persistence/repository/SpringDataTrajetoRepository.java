package io.github.tinhascode.essarota.infrastructure.persistence.repository;

import io.github.tinhascode.essarota.infrastructure.persistence.entity.TrajetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataTrajetoRepository extends JpaRepository<TrajetoEntity, UUID> {

    List<TrajetoEntity> findByUsuarioId(UUID usuarioId);

    boolean existsByIdAndUsuarioId(UUID id, UUID usuarioId);
}
