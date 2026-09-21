package io.github.tinhascode.essarota.infrastructure.persistence;

import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.TrajetoEntity;
import io.github.tinhascode.essarota.infrastructure.persistence.mapper.TrajetoEntityMapper;
import io.github.tinhascode.essarota.infrastructure.persistence.repository.SpringDataTrajetoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TrajetoRepositoryImpl implements TrajetoRepository {

    private final SpringDataTrajetoRepository springDataTrajetoRepository;
    private final TrajetoEntityMapper trajetoEntityMapper;

    public TrajetoRepositoryImpl(
            SpringDataTrajetoRepository springDataTrajetoRepository,
            TrajetoEntityMapper trajetoEntityMapper
    ) {
        this.springDataTrajetoRepository = springDataTrajetoRepository;
        this.trajetoEntityMapper = trajetoEntityMapper;
    }

    @Override
    public Trajeto salvar(Trajeto trajeto) {
        TrajetoEntity entity = trajetoEntityMapper.toEntity(trajeto);
        TrajetoEntity salvo = springDataTrajetoRepository.save(entity);
        return trajetoEntityMapper.toDomain(salvo);
    }

    @Override
    public Optional<Trajeto> buscarPorId(UUID id) {
        return springDataTrajetoRepository.findById(id)
                .map(trajetoEntityMapper::toDomain);
    }

    @Override
    public List<Trajeto> listarPorUsuarioId(UUID usuarioId) {
        List<TrajetoEntity> entities = springDataTrajetoRepository.findByUsuarioId(usuarioId);
        return trajetoEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataTrajetoRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return springDataTrajetoRepository.existsById(id);
    }

    @Override
    public boolean pertenceAoUsuario(UUID trajetoId, UUID usuarioId) {
        return springDataTrajetoRepository.existsByIdAndUsuarioId(trajetoId, usuarioId);
    }
}
