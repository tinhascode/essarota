package io.github.tinhascode.essarota.infrastructure.security;

import io.github.tinhascode.essarota.domain.service.PasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String codificar(String senhaPura) {
        return passwordEncoder.encode(senhaPura);
    }

    @Override
    public boolean validar(String senhaPura, String senhaCodificada) {
        return passwordEncoder.matches(senhaPura, senhaCodificada);
    }
}
