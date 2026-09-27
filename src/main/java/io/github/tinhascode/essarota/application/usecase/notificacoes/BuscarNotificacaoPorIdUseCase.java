package io.github.tinhascode.essarota.application.usecase.notificacoes;

import io.github.tinhascode.essarota.application.dto.notificacoes.NotificacaoResponse;
import io.github.tinhascode.essarota.application.mapper.NotificacaoDtoMapper;
import io.github.tinhascode.essarota.domain.exception.NotificacaoNaoEncontradaException;
import io.github.tinhascode.essarota.domain.model.Notificacao;
import io.github.tinhascode.essarota.domain.repository.NotificacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarNotificacaoPorIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(BuscarNotificacaoPorIdUseCase.class);

    private final NotificacaoRepository notificacaoRepository;
    private final NotificacaoDtoMapper notificacaoDtoMapper;

    public BuscarNotificacaoPorIdUseCase(NotificacaoRepository notificacaoRepository, NotificacaoDtoMapper notificacaoDtoMapper) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoDtoMapper = notificacaoDtoMapper;
    }

    @Transactional(readOnly = true)
    public NotificacaoResponse executar(UUID id) {
        log.debug("Executando BuscarNotificacaoPorIdUseCase para ID: {}", id);
        Notificacao notificacao = notificacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotificacaoNaoEncontradaException(id));
        log.info("Notificação encontrada. ID: {}, Usuario: {}", notificacao.getId(), notificacao.getUsuarioId());
        return notificacaoDtoMapper.toResponse(notificacao);
    }
}
