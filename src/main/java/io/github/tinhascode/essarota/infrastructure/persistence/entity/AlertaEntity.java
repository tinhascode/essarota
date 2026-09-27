package io.github.tinhascode.essarota.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "alertas")
public class AlertaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "linha_id", nullable = false)
    private UUID linhaId;

    @Column(name = "descricao", nullable = false, length = 500)
    private String descricao;

    @Column(name = "severidade", nullable = false, length = 30)
    private String severidade;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    public AlertaEntity() {
    }

    public AlertaEntity(UUID id, UUID linhaId, String descricao, String severidade, Instant criadoEm) {
        this.id = id;
        this.linhaId = linhaId;
        this.descricao = descricao;
        this.severidade = severidade;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getLinhaId() {
        return linhaId;
    }

    public void setLinhaId(UUID linhaId) {
        this.linhaId = linhaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSeveridade() {
        return severidade;
    }

    public void setSeveridade(String severidade) {
        this.severidade = severidade;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertaEntity that = (AlertaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
