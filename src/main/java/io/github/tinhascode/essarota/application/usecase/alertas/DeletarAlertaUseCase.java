package io.github.tinhascode.essarota.application.usecase.alertas;

import io.github.tinhascode.essarota.domain.exception.AlertaNaoEncontradoException;
import io.github.tinhascode.essarota.domain.repository.AlertaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeletarAlertaUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeletarAlertaUseCase.class);

    private final AlertaRepository alertaRepository;

    public DeletarAlertaUseCase(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Transactional
    public void executar(UUID id) {
        log.debug("Executando DeletarAlertaUseCase para ID: {}", id);
        if (!alertaRepository.existePorId(id)) {
            throw new AlertaNaoEncontradoException(id);
        }
        alertaRepository.deletarPorId(id);
        log.info("Alerta excluído com sucesso. ID: {}", id);
    }
}
