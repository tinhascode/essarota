package io.github.tinhascode.essarota.infrastructure.persistence;

import io.github.tinhascode.essarota.domain.model.TrajetoLinha;
import io.github.tinhascode.essarota.domain.repository.TrajetoLinhaRepository;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.TrajetoLinhaEntity;
import io.github.tinhascode.essarota.infrastructure.persistence.mapper.TrajetoLinhaEntityMapper;
import io.github.tinhascode.essarota.infrastructure.persistence.repository.SpringDataTrajetoLinhaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TrajetoLinhaRepositoryImpl implements TrajetoLinhaRepository {

    private final SpringDataTrajetoLinhaRepository springDataTrajetoLinhaRepository;
    private final TrajetoLinhaEntityMapper trajetoLinhaEntityMapper;

    public TrajetoLinhaRepositoryImpl(
            SpringDataTrajetoLinhaRepository springDataTrajetoLinhaRepository,
            TrajetoLinhaEntityMapper trajetoLinhaEntityMapper
    ) {
        this.springDataTrajetoLinhaRepository = springDataTrajetoLinhaRepository;
        this.trajetoLinhaEntityMapper = trajetoLinhaEntityMapper;
    }

    @Override
    public TrajetoLinha salvar(TrajetoLinha trajetoLinha) {
        TrajetoLinhaEntity entity = trajetoLinhaEntityMapper.toEntity(trajetoLinha);
        TrajetoLinhaEntity salvo = springDataTrajetoLinhaRepository.save(entity);
        return trajetoLinhaEntityMapper.toDomain(salvo);
    }

    @Override
    public Optional<TrajetoLinha> buscarPorId(UUID id) {
        return springDataTrajetoLinhaRepository.findById(id)
                .map(trajetoLinhaEntityMapper::toDomain);
    }

    @Override
    public List<TrajetoLinha> listarPorTrajetoId(UUID trajetoId) {
        List<TrajetoLinhaEntity> entities = springDataTrajetoLinhaRepository.findByTrajetoIdOrderByOrdemAsc(trajetoId);
        return trajetoLinhaEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataTrajetoLinhaRepository.deleteById(id);
    }

    @Override
    public void deletarPorTrajetoIdELinhaId(UUID trajetoId, UUID linhaId) {
        springDataTrajetoLinhaRepository.deleteByTrajetoIdAndLinhaId(trajetoId, linhaId);
    }

    @Override
    public boolean existePorTrajetoIdELinhaId(UUID trajetoId, UUID linhaId) {
        return springDataTrajetoLinhaRepository.existsByTrajetoIdAndLinhaId(trajetoId, linhaId);
    }

    @Override
    public Optional<TrajetoLinha> buscarPorTrajetoIdELinhaId(UUID trajetoId, UUID linhaId) {
        return springDataTrajetoLinhaRepository.findByTrajetoIdAndLinhaId(trajetoId, linhaId)
                .map(trajetoLinhaEntityMapper::toDomain);
    }
}
