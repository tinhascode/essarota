package io.github.tinhascode.essarota.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "trajetos_linhas",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_trajeto_linha", columnNames = {"trajeto_id", "linha_id"})
        }
)
public class TrajetoLinhaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "trajeto_id", nullable = false)
    private UUID trajetoId;

    @Column(name = "linha_id", nullable = false)
    private UUID linhaId;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    public TrajetoLinhaEntity() {
    }

    public TrajetoLinhaEntity(UUID id, UUID trajetoId, UUID linhaId, Integer ordem) {
        this.id = id;
        this.trajetoId = trajetoId;
        this.linhaId = linhaId;
        this.ordem = ordem;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTrajetoId() {
        return trajetoId;
    }

    public void setTrajetoId(UUID trajetoId) {
        this.trajetoId = trajetoId;
    }

    public UUID getLinhaId() {
        return linhaId;
    }

    public void setLinhaId(UUID linhaId) {
        this.linhaId = linhaId;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrajetoLinhaEntity that = (TrajetoLinhaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
