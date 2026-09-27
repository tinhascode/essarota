package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class TrajetoLinhaNaoEncontradaException extends DomainException {
    public TrajetoLinhaNaoEncontradaException(UUID trajetoId, UUID linhaId) {
        super("Associação não encontrada entre o trajeto ID '" + trajetoId + "' e a linha ID '" + linhaId + "'.");
    }

    public TrajetoLinhaNaoEncontradaException(UUID id) {
        super("Associação de trajeto e linha não encontrada com o ID: " + id);
    }
}
