package io.github.tinhascode.essarota.domain.exception;

public class EmailJaCadastradoException extends DomainException {
    public EmailJaCadastradoException(String email) {
        super("Já existe um usuário cadastrado com o e-mail: " + email);
    }
}
