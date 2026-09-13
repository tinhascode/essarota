package io.github.tinhascode.essarota.domain.exception;

import java.util.UUID;

public class UsuarioNaoEncontradoException extends DomainException {
    public UsuarioNaoEncontradoException(UUID id) {
        super("Usuário não encontrado com o ID: " + id);
    }
}
