package io.github.tinhascode.essarota.infrastructure.persistence.repository;

import io.github.tinhascode.essarota.infrastructure.persistence.entity.LinhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataLinhaRepository extends JpaRepository<LinhaEntity, UUID> {
}
