package io.github.tinhascode.essarota.application.usecase.alertas;

import io.github.tinhascode.essarota.application.dto.alertas.AlertaResponse;
import io.github.tinhascode.essarota.application.mapper.AlertaDtoMapper;
import io.github.tinhascode.essarota.domain.exception.AlertaNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Alerta;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarAlertaPorIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(BuscarAlertaPorIdUseCase.class);

    private final AlertaRepository alertaRepository;
    private final AlertaDtoMapper alertaDtoMapper;

    public BuscarAlertaPorIdUseCase(AlertaRepository alertaRepository, AlertaDtoMapper alertaDtoMapper) {
        this.alertaRepository = alertaRepository;
        this.alertaDtoMapper = alertaDtoMapper;
    }

    @Transactional(readOnly = true)
    public AlertaResponse executar(UUID id) {
        log.debug("Executando BuscarAlertaPorIdUseCase para ID: {}", id);
        Alerta alerta = alertaRepository.buscarPorId(id)
                .orElseThrow(() -> new AlertaNaoEncontradoException(id));
        log.info("Alerta encontrado. ID: {}, Linha: {}", alerta.getId(), alerta.getLinhaId());
        return alertaDtoMapper.toResponse(alerta);
    }
}
