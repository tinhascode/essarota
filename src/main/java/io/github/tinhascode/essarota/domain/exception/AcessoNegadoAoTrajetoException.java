package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class AcessoNegadoAoTrajetoException extends DomainException {
    public AcessoNegadoAoTrajetoException(UUID trajetoId) {
        super("Acesso negado: o trajeto " + trajetoId + " não pertence ao usuário autenticado.");
    }
}
