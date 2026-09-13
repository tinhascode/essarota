package io.github.tinhascode.essarota.domain.service;

import io.github.tinhascode.essarota.domain.model.Usuario;

import java.util.UUID;

public interface TokenService {

    String gerarToken(Usuario usuario);

    boolean isTokenValido(String token);

    String extrairEmail(String token);

    UUID extrairUsuarioId(String token);
}
