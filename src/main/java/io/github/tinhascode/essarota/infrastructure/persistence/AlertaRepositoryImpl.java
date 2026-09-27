package io.github.tinhascode.essarota.infrastructure.persistence;

import io.github.tinhascode.essarota.domain.model.Alerta;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.AlertaEntity;
import io.github.tinhascode.essarota.infrastructure.persistence.mapper.AlertaEntityMapper;
import io.github.tinhascode.essarota.infrastructure.persistence.repository.SpringDataAlertaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AlertaRepositoryImpl implements AlertaRepository {

    private final SpringDataAlertaRepository springDataAlertaRepository;
    private final AlertaEntityMapper alertaEntityMapper;

    public AlertaRepositoryImpl(
            SpringDataAlertaRepository springDataAlertaRepository,
            AlertaEntityMapper alertaEntityMapper
    ) {
        this.springDataAlertaRepository = springDataAlertaRepository;
        this.alertaEntityMapper = alertaEntityMapper;
    }

    @Override
    public Alerta salvar(Alerta alerta) {
        AlertaEntity entity = alertaEntityMapper.toEntity(alerta);
        AlertaEntity salvo = springDataAlertaRepository.save(entity);
        return alertaEntityMapper.toDomain(salvo);
    }

    @Override
    public Optional<Alerta> buscarPorId(UUID id) {
        return springDataAlertaRepository.findById(id)
                .map(alertaEntityMapper::toDomain);
    }

    @Override
    public List<Alerta> listarPorLinhaId(UUID linhaId) {
        List<AlertaEntity> entities = springDataAlertaRepository.findByLinhaIdOrderByCriadoEmDesc(linhaId);
        return alertaEntityMapper.toDomainList(entities);
    }

    @Override
    public List<Alerta> listarTodos() {
        List<AlertaEntity> entities = springDataAlertaRepository.findAll();
        return alertaEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataAlertaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return springDataAlertaRepository.existsById(id);
    }
}
