package io.github.tinhascode.essarota.application.usecase.alertas;

import io.github.tinhascode.essarota.application.dto.alertas.AlertaResponse;
import io.github.tinhascode.essarota.application.mapper.AlertaDtoMapper;
import io.github.tinhascode.essarota.domain.exception.LinhaNaoEncontradaException;
import io.github.tinhascode.essarota.domain.model.Alerta;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListarAlertasPorLinhaUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarAlertasPorLinhaUseCase.class);

    private final AlertaRepository alertaRepository;
    private final LinhaRepository linhaRepository;
    private final AlertaDtoMapper alertaDtoMapper;

    public ListarAlertasPorLinhaUseCase(
            AlertaRepository alertaRepository,
            LinhaRepository linhaRepository,
            AlertaDtoMapper alertaDtoMapper
    ) {
        this.alertaRepository = alertaRepository;
        this.linhaRepository = linhaRepository;
        this.alertaDtoMapper = alertaDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<AlertaResponse> executar(UUID linhaId) {
        log.debug("Executando ListarAlertasPorLinhaUseCase para linha ID: {}", linhaId);
        if (!linhaRepository.existePorId(linhaId)) {
            throw new LinhaNaoEncontradaException(linhaId);
        }
        List<Alerta> alertas = alertaRepository.listarPorLinhaId(linhaId);
        log.info("Listagem retornou {} alertas para a linha {}", alertas.size(), linhaId);
        return alertaDtoMapper.toResponseList(alertas);
    }
}
