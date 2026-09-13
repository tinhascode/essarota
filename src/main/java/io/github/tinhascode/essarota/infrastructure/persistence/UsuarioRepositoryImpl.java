package io.github.tinhascode.essarota.infrastructure.persistence;

import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.UsuarioEntity;
import io.github.tinhascode.essarota.infrastructure.persistence.mapper.UsuarioEntityMapper;
import io.github.tinhascode.essarota.infrastructure.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final SpringDataUsuarioRepository springDataUsuarioRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    public UsuarioRepositoryImpl(
            SpringDataUsuarioRepository springDataUsuarioRepository,
            UsuarioEntityMapper usuarioEntityMapper
    ) {
        this.springDataUsuarioRepository = springDataUsuarioRepository;
        this.usuarioEntityMapper = usuarioEntityMapper;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = usuarioEntityMapper.toEntity(usuario);
        UsuarioEntity salvo = springDataUsuarioRepository.save(entity);
        return usuarioEntityMapper.toDomain(salvo);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return springDataUsuarioRepository.findById(id)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return springDataUsuarioRepository.findByEmailIgnoreCase(email.trim())
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        List<UsuarioEntity> entities = springDataUsuarioRepository.findAll();
        return usuarioEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataUsuarioRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return springDataUsuarioRepository.existsById(id);
    }

    @Override
    public boolean existePorEmail(String email) {
        if (email == null) {
            return false;
        }
        return springDataUsuarioRepository.existsByEmailIgnoreCase(email.trim());
    }
}
