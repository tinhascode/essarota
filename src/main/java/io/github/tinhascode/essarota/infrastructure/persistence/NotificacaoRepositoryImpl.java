package io.github.tinhascode.essarota.infrastructure.persistence;

import io.github.tinhascode.essarota.domain.model.Notificacao;
import io.github.tinhascode.essarota.domain.repository.NotificacaoRepository;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.NotificacaoEntity;
import io.github.tinhascode.essarota.infrastructure.persistence.mapper.NotificacaoEntityMapper;
import io.github.tinhascode.essarota.infrastructure.persistence.repository.SpringDataNotificacaoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class NotificacaoRepositoryImpl implements NotificacaoRepository {

    private final SpringDataNotificacaoRepository springDataNotificacaoRepository;
    private final NotificacaoEntityMapper notificacaoEntityMapper;

    public NotificacaoRepositoryImpl(
            SpringDataNotificacaoRepository springDataNotificacaoRepository,
            NotificacaoEntityMapper notificacaoEntityMapper
    ) {
        this.springDataNotificacaoRepository = springDataNotificacaoRepository;
        this.notificacaoEntityMapper = notificacaoEntityMapper;
    }

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        NotificacaoEntity entity = notificacaoEntityMapper.toEntity(notificacao);
        NotificacaoEntity salvo = springDataNotificacaoRepository.save(entity);
        return notificacaoEntityMapper.toDomain(salvo);
    }

    @Override
    public Optional<Notificacao> buscarPorId(UUID id) {
        return springDataNotificacaoRepository.findById(id)
                .map(notificacaoEntityMapper::toDomain);
    }

    @Override
    public List<Notificacao> listarPorUsuarioId(UUID usuarioId) {
        List<NotificacaoEntity> entities = springDataNotificacaoRepository.findByUsuarioIdOrderByEnviadoEmDesc(usuarioId);
        return notificacaoEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataNotificacaoRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return springDataNotificacaoRepository.existsById(id);
    }
}
