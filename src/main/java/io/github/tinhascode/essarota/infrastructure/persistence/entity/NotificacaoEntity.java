package io.github.tinhascode.essarota.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notificacoes")
public class NotificacaoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "alerta_id", nullable = false)
    private UUID alertaId;

    @Column(name = "canal", nullable = false, length = 30)
    private String canal;

    @Column(name = "enviado_em", nullable = false, updatable = false)
    private Instant enviadoEm;

    public NotificacaoEntity() {
    }

    public NotificacaoEntity(UUID id, UUID usuarioId, UUID alertaId, String canal, Instant enviadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.alertaId = alertaId;
        this.canal = canal;
        this.enviadoEm = enviadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getAlertaId() {
        return alertaId;
    }

    public void setAlertaId(UUID alertaId) {
        this.alertaId = alertaId;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Instant getEnviadoEm() {
        return enviadoEm;
    }

    public void setEnviadoEm(Instant enviadoEm) {
        this.enviadoEm = enviadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificacaoEntity that = (NotificacaoEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
