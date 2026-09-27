package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class AlertaNaoEncontradoException extends DomainException {
    public AlertaNaoEncontradoException(UUID id) {
        super("Alerta não encontrado com o ID: " + id);
    }
}
