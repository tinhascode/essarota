package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class NotificacaoNaoEncontradaException extends DomainException {
    public NotificacaoNaoEncontradaException(UUID id) {
        super("Notificação não encontrada com o ID: " + id);
    }
}
