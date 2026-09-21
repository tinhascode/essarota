package io.github.tinhascode.essarota.domain.model;

import io.github.tinhascode.essarota.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public class Trajeto {

    private final UUID id;
    private final UUID usuarioId;
    private String origem;
    private String destino;
    private Integer tempoEstimadoMinutos;

    public Trajeto(UUID id, UUID usuarioId, String origem, String destino, Integer tempoEstimadoMinutos) {
        if (usuarioId == null) {
            throw new DomainException("O trajeto deve estar associado a um usuário.");
        }
        validarOrigem(origem);
        validarDestino(destino);
        validarTempoEstimado(tempoEstimadoMinutos);

        this.id = id != null ? id : UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.origem = origem.trim();
        this.destino = destino.trim();
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
    }

    public static Trajeto criar(UUID usuarioId, String origem, String destino, Integer tempoEstimadoMinutos) {
        return new Trajeto(UUID.randomUUID(), usuarioId, origem, destino, tempoEstimadoMinutos);
    }

    public void atualizar(String novaOrigem, String novoDestino, Integer novoTempoEstimadoMinutos) {
        validarOrigem(novaOrigem);
        validarDestino(novoDestino);
        validarTempoEstimado(novoTempoEstimadoMinutos);

        this.origem = novaOrigem.trim();
        this.destino = novoDestino.trim();
        this.tempoEstimadoMinutos = novoTempoEstimadoMinutos;
    }

    public boolean pertenceAoUsuario(UUID usuarioIdComparar) {
        return this.usuarioId != null && this.usuarioId.equals(usuarioIdComparar);
    }

    private void validarOrigem(String origem) {
        if (origem == null || origem.trim().isBlank()) {
            throw new DomainException("A origem do trajeto é obrigatória.");
        }
    }

    private void validarDestino(String destino) {
        if (destino == null || destino.trim().isBlank()) {
            throw new DomainException("O destino do trajeto é obrigatório.");
        }
    }

    private void validarTempoEstimado(Integer tempoEstimadoMinutos) {
        if (tempoEstimadoMinutos != null && tempoEstimadoMinutos <= 0) {
            throw new DomainException("O tempo estimado em minutos deve ser positivo.");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public Integer getTempoEstimadoMinutos() {
        return tempoEstimadoMinutos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trajeto trajeto = (Trajeto) o;
        return Objects.equals(id, trajeto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
