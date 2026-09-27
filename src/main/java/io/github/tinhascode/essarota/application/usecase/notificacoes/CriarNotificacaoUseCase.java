package io.github.tinhascode.essarota.application.usecase.notificacoes;

import io.github.tinhascode.essarota.application.dto.notificacoes.CriarNotificacaoRequest;
import io.github.tinhascode.essarota.application.dto.notificacoes.NotificacaoResponse;
import io.github.tinhascode.essarota.application.mapper.NotificacaoDtoMapper;
import io.github.tinhascode.essarota.domain.exception.AlertaNaoEncontradoException;
import io.github.tinhascode.essarota.domain.exception.UsuarioNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Notificacao;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import io.github.tinhascode.essarota.domain.repository.NotificacaoRepository;
import io.github.tinhascode.essarota.domain.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarNotificacaoUseCase {

    private static final Logger log = LoggerFactory.getLogger(CriarNotificacaoUseCase.class);

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlertaRepository alertaRepository;
    private final NotificacaoDtoMapper notificacaoDtoMapper;

    public CriarNotificacaoUseCase(
            NotificacaoRepository notificacaoRepository,
            UsuarioRepository usuarioRepository,
            AlertaRepository alertaRepository,
            NotificacaoDtoMapper notificacaoDtoMapper
    ) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alertaRepository = alertaRepository;
        this.notificacaoDtoMapper = notificacaoDtoMapper;
    }

    @Transactional
    public NotificacaoResponse executar(CriarNotificacaoRequest request) {
        log.debug("Executando CriarNotificacaoUseCase para Usuario: {}, Alerta: {}, Canal: {}",
                request.usuarioId(), request.alertaId(), request.canal());

        if (!usuarioRepository.buscarPorId(request.usuarioId()).isPresent()) {
            throw new UsuarioNaoEncontradoException(request.usuarioId());
        }

        if (!alertaRepository.existePorId(request.alertaId())) {
            throw new AlertaNaoEncontradoException(request.alertaId());
        }

        Notificacao notificacao = Notificacao.criar(request.usuarioId(), request.alertaId(), request.canal());
        Notificacao salva = notificacaoRepository.salvar(notificacao);
        log.info("Notificação registrada com sucesso. ID: {}, Usuario: {}, Canal: {}",
                salva.getId(), salva.getUsuarioId(), salva.getCanal());
        return notificacaoDtoMapper.toResponse(salva);
    }
}
