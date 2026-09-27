package io.github.tinhascode.essarota.domain.model;

import io.github.tinhascode.essarota.domain.exception.DomainException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Alerta {

    private final UUID id;
    private final UUID linhaId;
    private String descricao;
    private String severidade;
    private final Instant criadoEm;

    public Alerta(UUID id, UUID linhaId, String descricao, String severidade, Instant criadoEm) {
        if (linhaId == null) {
            throw new DomainException("A linha associada ao alerta é obrigatória.");
        }
        validarDescricao(descricao);
        validarSeveridade(severidade);

        this.id = id != null ? id : UUID.randomUUID();
        this.linhaId = linhaId;
        this.descricao = descricao.trim();
        this.severidade = severidade.trim().toUpperCase();
        this.criadoEm = criadoEm != null ? criadoEm : Instant.now();
    }

    public static Alerta criar(UUID linhaId, String descricao, String severidade) {
        return new Alerta(UUID.randomUUID(), linhaId, descricao, severidade, Instant.now());
    }

    public void atualizar(String novaDescricao, String novaSeveridade) {
        validarDescricao(novaDescricao);
        validarSeveridade(novaSeveridade);
        this.descricao = novaDescricao.trim();
        this.severidade = novaSeveridade.trim().toUpperCase();
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isBlank()) {
            throw new DomainException("A descrição do alerta é obrigatória.");
        }
    }

    private void validarSeveridade(String severidade) {
        if (severidade == null || severidade.trim().isBlank()) {
            throw new DomainException("A severidade do alerta é obrigatória (ex: BAIXA, MEDIA, ALTA, GRAVE).");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getLinhaId() {
        return linhaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSeveridade() {
        return severidade;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerta alerta = (Alerta) o;
        return Objects.equals(id, alerta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
