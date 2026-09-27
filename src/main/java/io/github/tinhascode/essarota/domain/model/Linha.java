package io.github.tinhascode.essarota.domain.model;

import io.github.tinhascode.essarota.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public class Linha {

    private final UUID id;
    private String nome;
    private String tipo;

    public Linha(UUID id, String nome, String tipo) {
        validarNome(nome);
        validarTipo(tipo);
        this.id = id != null ? id : UUID.randomUUID();
        this.nome = nome.trim();
        this.tipo = tipo.trim().toUpperCase();
    }

    public static Linha criar(String nome, String tipo) {
        return new Linha(UUID.randomUUID(), nome, tipo);
    }

    public void atualizar(String novoNome, String novoTipo) {
        validarNome(novoNome);
        validarTipo(novoTipo);
        this.nome = novoNome.trim();
        this.tipo = novoTipo.trim().toUpperCase();
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isBlank()) {
            throw new DomainException("O nome da linha é obrigatório.");
        }
        if (nome.trim().length() < 2) {
            throw new DomainException("O nome da linha deve conter ao menos 2 caracteres.");
        }
    }

    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isBlank()) {
            throw new DomainException("O tipo da linha é obrigatório (ex: METRO, TREM, ONIBUS).");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Linha linha = (Linha) o;
        return Objects.equals(id, linha.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
