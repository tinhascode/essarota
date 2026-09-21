package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class TrajetoNaoEncontradoException extends DomainException {
    public TrajetoNaoEncontradoException(UUID id) {
        super("Trajeto não encontrado com o ID: " + id);
    }
}
