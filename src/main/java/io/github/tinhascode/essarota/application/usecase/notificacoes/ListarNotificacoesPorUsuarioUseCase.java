package io.github.tinhascode.essarota.application.usecase.notificacoes;

import io.github.tinhascode.essarota.application.dto.notificacoes.NotificacaoResponse;
import io.github.tinhascode.essarota.application.mapper.NotificacaoDtoMapper;
import io.github.tinhascode.essarota.domain.model.Notificacao;
import io.github.tinhascode.essarota.domain.repository.NotificacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListarNotificacoesPorUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarNotificacoesPorUsuarioUseCase.class);

    private final NotificacaoRepository notificacaoRepository;
    private final NotificacaoDtoMapper notificacaoDtoMapper;

    public ListarNotificacoesPorUsuarioUseCase(
            NotificacaoRepository notificacaoRepository,
            NotificacaoDtoMapper notificacaoDtoMapper
    ) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoDtoMapper = notificacaoDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> executar(UUID usuarioId) {
        log.debug("Executando ListarNotificacoesPorUsuarioUseCase para Usuario: {}", usuarioId);
        List<Notificacao> lista = notificacaoRepository.listarPorUsuarioId(usuarioId);
        log.info("Listagem retornou {} notificações para o usuário {}", lista.size(), usuarioId);
        return notificacaoDtoMapper.toResponseList(lista);
    }
}
