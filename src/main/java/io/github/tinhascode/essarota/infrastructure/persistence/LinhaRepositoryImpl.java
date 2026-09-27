package io.github.tinhascode.essarota.infrastructure.persistence;

import io.github.tinhascode.essarota.domain.model.Linha;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.LinhaEntity;
import io.github.tinhascode.essarota.infrastructure.persistence.mapper.LinhaEntityMapper;
import io.github.tinhascode.essarota.infrastructure.persistence.repository.SpringDataLinhaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class LinhaRepositoryImpl implements LinhaRepository {

    private final SpringDataLinhaRepository springDataLinhaRepository;
    private final LinhaEntityMapper linhaEntityMapper;

    public LinhaRepositoryImpl(
            SpringDataLinhaRepository springDataLinhaRepository,
            LinhaEntityMapper linhaEntityMapper
    ) {
        this.springDataLinhaRepository = springDataLinhaRepository;
        this.linhaEntityMapper = linhaEntityMapper;
    }

    @Override
    public Linha salvar(Linha linha) {
        LinhaEntity entity = linhaEntityMapper.toEntity(linha);
        LinhaEntity salvo = springDataLinhaRepository.save(entity);
        return linhaEntityMapper.toDomain(salvo);
    }

    @Override
    public Optional<Linha> buscarPorId(UUID id) {
        return springDataLinhaRepository.findById(id)
                .map(linhaEntityMapper::toDomain);
    }

    @Override
    public List<Linha> listarTodas() {
        List<LinhaEntity> entities = springDataLinhaRepository.findAll();
        return linhaEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataLinhaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return springDataLinhaRepository.existsById(id);
    }
}
