package io.github.tinhascode.essarota.application.usecase.alertas;

import io.github.tinhascode.essarota.application.dto.alertas.AlertaResponse;
import io.github.tinhascode.essarota.application.dto.alertas.CriarAlertaRequest;
import io.github.tinhascode.essarota.application.mapper.AlertaDtoMapper;
import io.github.tinhascode.essarota.domain.exception.LinhaNaoEncontradaException;
import io.github.tinhascode.essarota.domain.model.Alerta;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarAlertaUseCase {

    private static final Logger log = LoggerFactory.getLogger(CriarAlertaUseCase.class);

    private final AlertaRepository alertaRepository;
    private final LinhaRepository linhaRepository;
    private final AlertaDtoMapper alertaDtoMapper;

    public CriarAlertaUseCase(
            AlertaRepository alertaRepository,
            LinhaRepository linhaRepository,
            AlertaDtoMapper alertaDtoMapper
    ) {
        this.alertaRepository = alertaRepository;
        this.linhaRepository = linhaRepository;
        this.alertaDtoMapper = alertaDtoMapper;
    }

    @Transactional
    public AlertaResponse executar(CriarAlertaRequest request) {
        log.debug("Executando CriarAlertaUseCase para Linha: {}, Severidade: {}", request.linhaId(), request.severidade());

        if (!linhaRepository.existePorId(request.linhaId())) {
            throw new LinhaNaoEncontradaException(request.linhaId());
        }

        Alerta alerta = Alerta.criar(request.linhaId(), request.descricao(), request.severidade());
        Alerta salvo = alertaRepository.salvar(alerta);
        log.info("Alerta criado com sucesso. ID: {}, Linha: {}, Severidade: {}", salvo.getId(), salvo.getLinhaId(), salvo.getSeveridade());
        return alertaDtoMapper.toResponse(salvo);
    }
}
