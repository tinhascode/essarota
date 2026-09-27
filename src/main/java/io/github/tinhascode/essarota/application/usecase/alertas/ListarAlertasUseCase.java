package io.github.tinhascode.essarota.application.usecase.alertas;

import io.github.tinhascode.essarota.application.dto.alertas.AlertaResponse;
import io.github.tinhascode.essarota.application.mapper.AlertaDtoMapper;
import io.github.tinhascode.essarota.domain.model.Alerta;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarAlertasUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarAlertasUseCase.class);

    private final AlertaRepository alertaRepository;
    private final AlertaDtoMapper alertaDtoMapper;

    public ListarAlertasUseCase(AlertaRepository alertaRepository, AlertaDtoMapper alertaDtoMapper) {
        this.alertaRepository = alertaRepository;
        this.alertaDtoMapper = alertaDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<AlertaResponse> executar() {
        log.debug("Executando ListarAlertasUseCase");
        List<Alerta> alertas = alertaRepository.listarTodos();
        log.info("Listagem de alertas retornou {} registros", alertas.size());
        return alertaDtoMapper.toResponseList(alertas);
    }
}
