package io.github.tinhascode.essarota.infrastructure.persistence.repository;

import io.github.tinhascode.essarota.infrastructure.persistence.entity.TrajetoLinhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataTrajetoLinhaRepository extends JpaRepository<TrajetoLinhaEntity, UUID> {

    List<TrajetoLinhaEntity> findByTrajetoIdOrderByOrdemAsc(UUID trajetoId);

    void deleteByTrajetoIdAndLinhaId(UUID trajetoId, UUID linhaId);

    boolean existsByTrajetoIdAndLinhaId(UUID trajetoId, UUID linhaId);

    Optional<TrajetoLinhaEntity> findByTrajetoIdAndLinhaId(UUID trajetoId, UUID linhaId);
}
