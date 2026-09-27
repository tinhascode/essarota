package io.github.tinhascode.essarota.application.usecase.linhas;

import io.github.tinhascode.essarota.domain.exception.LinhaNaoEncontradaException;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeletarLinhaUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeletarLinhaUseCase.class);

    private final LinhaRepository linhaRepository;

    public DeletarLinhaUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }

    @Transactional
    public void executar(UUID id) {
        log.debug("Executando DeletarLinhaUseCase para ID: {}", id);
        if (!linhaRepository.existePorId(id)) {
            throw new LinhaNaoEncontradaException(id);
        }
        linhaRepository.deletarPorId(id);
        log.info("Linha excluída com sucesso. ID: {}", id);
    }
}
