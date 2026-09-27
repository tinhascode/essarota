package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class LinhaNaoEncontradaException extends DomainException {
    public LinhaNaoEncontradaException(UUID id) {
        super("Linha não encontrada com o ID: " + id);
    }
}
