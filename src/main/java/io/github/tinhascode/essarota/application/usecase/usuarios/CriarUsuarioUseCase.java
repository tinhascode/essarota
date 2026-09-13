package io.github.tinhascode.essarota.application.usecase.usuarios;

import io.github.tinhascode.essarota.application.dto.usuarios.CriarUsuarioRequest;
import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.application.mapper.UsuarioDtoMapper;
import io.github.tinhascode.essarota.domain.exception.EmailJaCadastradoException;
import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import io.github.tinhascode.essarota.domain.service.PasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDtoMapper usuarioDtoMapper;
    private final PasswordService passwordService;

    public CriarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            UsuarioDtoMapper usuarioDtoMapper,
            PasswordService passwordService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
        this.passwordService = passwordService;
    }

    @Transactional
    public UsuarioResponse executar(CriarUsuarioRequest request) {
        if (usuarioRepository.existePorEmail(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        String senhaCriptografada = passwordService.codificar(request.senha());

        Usuario usuario = new Usuario(
                null,
                request.nome(),
                request.email(),
                senhaCriptografada,
                request.telefoneWhatsapp(),
                request.deviceToken()
        );

        Usuario salvo = usuarioRepository.salvar(usuario);
        return usuarioDtoMapper.toResponse(salvo);
    }
}
