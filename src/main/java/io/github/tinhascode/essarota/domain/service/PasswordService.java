package io.github.tinhascode.essarota.domain.service;

public interface PasswordService {

    String codificar(String senhaPura);

    boolean validar(String senhaPura, String senhaCodificada);
}
