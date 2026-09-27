package io.github.tinhascode.essarota.domain.model;

import io.github.tinhascode.essarota.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public class TrajetoLinha {

    private final UUID id;
    private final UUID trajetoId;
    private final UUID linhaId;
    private Integer ordem;

    public TrajetoLinha(UUID id, UUID trajetoId, UUID linhaId, Integer ordem) {
        if (trajetoId == null) {
            throw new DomainException("O trajeto é obrigatório para associar uma linha.");
        }
        if (linhaId == null) {
            throw new DomainException("A linha é obrigatória para a associação.");
        }
        validarOrdem(ordem);

        this.id = id != null ? id : UUID.randomUUID();
        this.trajetoId = trajetoId;
        this.linhaId = linhaId;
        this.ordem = ordem != null ? ordem : 1;
    }

    public static TrajetoLinha criar(UUID trajetoId, UUID linhaId, Integer ordem) {
        return new TrajetoLinha(UUID.randomUUID(), trajetoId, linhaId, ordem);
    }

    public void atualizarOrdem(Integer novaOrdem) {
        validarOrdem(novaOrdem);
        this.ordem = novaOrdem;
    }

    private void validarOrdem(Integer ordem) {
        if (ordem != null && ordem < 1) {
            throw new DomainException("A ordem da linha no trajeto deve ser igual ou maior que 1.");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getTrajetoId() {
        return trajetoId;
    }

    public UUID getLinhaId() {
        return linhaId;
    }

    public Integer getOrdem() {
        return ordem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrajetoLinha that = (TrajetoLinha) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
