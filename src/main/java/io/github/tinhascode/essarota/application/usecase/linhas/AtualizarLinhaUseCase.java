package io.github.tinhascode.essarota.application.usecase.linhas;

import io.github.tinhascode.essarota.application.dto.linhas.AtualizarLinhaRequest;
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
public class AtualizarLinhaUseCase {

    private static final Logger log = LoggerFactory.getLogger(AtualizarLinhaUseCase.class);

    private final LinhaRepository linhaRepository;
    private final LinhaDtoMapper linhaDtoMapper;

    public AtualizarLinhaUseCase(LinhaRepository linhaRepository, LinhaDtoMapper linhaDtoMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaDtoMapper = linhaDtoMapper;
    }

    @Transactional
    public LinhaResponse executar(UUID id, AtualizarLinhaRequest request) {
        log.debug("Executando AtualizarLinhaUseCase para ID: {}", id);
        Linha linha = linhaRepository.buscarPorId(id)
                .orElseThrow(() -> new LinhaNaoEncontradaException(id));

        linha.atualizar(request.nome(), request.tipo());
        Linha salva = linhaRepository.salvar(linha);
        log.info("Linha atualizada com sucesso. ID: {}, Novo Nome: '{}'", salva.getId(), salva.getNome());
        return linhaDtoMapper.toResponse(salva);
    }
}
