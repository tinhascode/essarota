package io.github.tinhascode.essarota.application.usecase.linhas;

import io.github.tinhascode.essarota.application.dto.linhas.LinhaResponse;
import io.github.tinhascode.essarota.application.mapper.LinhaDtoMapper;
import io.github.tinhascode.essarota.domain.exception.LinhaNaoEncontradaException;
import io.github.tinhascode.essarota.domain.model.Linha;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarLinhaPorIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(BuscarLinhaPorIdUseCase.class);

    private final LinhaRepository linhaRepository;
    private final LinhaDtoMapper linhaDtoMapper;

    public BuscarLinhaPorIdUseCase(LinhaRepository linhaRepository, LinhaDtoMapper linhaDtoMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaDtoMapper = linhaDtoMapper;
    }

    @Transactional(readOnly = true)
    public LinhaResponse executar(UUID id) {
        log.debug("Executando BuscarLinhaPorIdUseCase para ID: {}", id);
        Linha linha = linhaRepository.buscarPorId(id)
                .orElseThrow(() -> new LinhaNaoEncontradaException(id));
        log.info("Linha encontrada. ID: {}, Nome: '{}'", linha.getId(), linha.getNome());
        return linhaDtoMapper.toResponse(linha);
    }
}
