package io.github.tinhascode.essarota.domain.exception;

public class CredenciaisInvalidasException extends DomainException {
    public CredenciaisInvalidasException() {
        super("E-mail ou senha inválidos.");
    }
}
