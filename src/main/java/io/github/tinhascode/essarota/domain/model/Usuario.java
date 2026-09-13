package io.github.tinhascode.essarota.domain.model;

import io.github.tinhascode.essarota.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class Usuario {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final UUID id;
    private String nome;
    private String email;
    private String senha;
    private String telefoneWhatsapp;
    private String deviceToken;

    public Usuario(UUID id, String nome, String email, String senha, String telefoneWhatsapp, String deviceToken) {
        validarNome(nome);
        validarEmail(email);
        validarSenha(senha);
        this.id = id != null ? id : UUID.randomUUID();
        this.nome = nome.trim();
        this.email = email.trim().toLowerCase();
        this.senha = senha;
        this.telefoneWhatsapp = normalizar(telefoneWhatsapp);
        this.deviceToken = normalizar(deviceToken);
    }

    public static Usuario criar(String nome, String email, String senha, String telefoneWhatsapp, String deviceToken) {
        return new Usuario(UUID.randomUUID(), nome, email, senha, telefoneWhatsapp, deviceToken);
    }

    public void atualizar(String novoNome, String novoEmail, String novoTelefoneWhatsapp, String novoDeviceToken) {
        validarNome(novoNome);
        validarEmail(novoEmail);
        this.nome = novoNome.trim();
        this.email = novoEmail.trim().toLowerCase();
        this.telefoneWhatsapp = normalizar(novoTelefoneWhatsapp);
        this.deviceToken = normalizar(novoDeviceToken);
    }

    public void atualizarSenha(String novaSenha) {
        validarSenha(novaSenha);
        this.senha = novaSenha;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isBlank()) {
            throw new DomainException("O nome do usuário é obrigatório.");
        }
        if (nome.trim().length() < 2) {
            throw new DomainException("O nome do usuário deve conter ao menos 2 caracteres.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isBlank()) {
            throw new DomainException("O e-mail do usuário é obrigatório.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new DomainException("O e-mail informado é inválido.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new DomainException("A senha é obrigatória.");
        }
    }

    private String normalizar(String valor) {
        return (valor != null && !valor.isBlank()) ? valor.trim() : null;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTelefoneWhatsapp() {
        return telefoneWhatsapp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
