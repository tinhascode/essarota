package io.github.tinhascode.essarota.domain.model;

import io.github.tinhascode.essarota.domain.exception.DomainException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Notificacao {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID alertaId;
    private String canal;
    private final Instant enviadoEm;

    public Notificacao(UUID id, UUID usuarioId, UUID alertaId, String canal, Instant enviadoEm) {
        if (usuarioId == null) {
            throw new DomainException("O usuário destinatário da notificação é obrigatório.");
        }
        if (alertaId == null) {
            throw new DomainException("O alerta associado à notificação é obrigatório.");
        }
        validarCanal(canal);

        this.id = id != null ? id : UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.alertaId = alertaId;
        this.canal = canal.trim().toUpperCase();
        this.enviadoEm = enviadoEm != null ? enviadoEm : Instant.now();
    }

    public static Notificacao criar(UUID usuarioId, UUID alertaId, String canal) {
        return new Notificacao(UUID.randomUUID(), usuarioId, alertaId, canal, Instant.now());
    }

    private void validarCanal(String canal) {
        if (canal == null || canal.trim().isBlank()) {
            throw new DomainException("O canal da notificação é obrigatório (ex: WHATSAPP, PUSH).");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getAlertaId() {
        return alertaId;
    }

    public String getCanal() {
        return canal;
    }

    public Instant getEnviadoEm() {
        return enviadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notificacao that = (Notificacao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
