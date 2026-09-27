package io.github.tinhascode.essarota.infrastructure.persistence.repository;

import io.github.tinhascode.essarota.infrastructure.persistence.entity.AlertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataAlertaRepository extends JpaRepository<AlertaEntity, UUID> {

    List<AlertaEntity> findByLinhaIdOrderByCriadoEmDesc(UUID linhaId);
}
