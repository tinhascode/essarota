package io.github.tinhascode.essarota.application.usecase.linhas;

import io.github.tinhascode.essarota.application.dto.linhas.CriarLinhaRequest;
import io.github.tinhascode.essarota.application.dto.linhas.LinhaResponse;
import io.github.tinhascode.essarota.application.mapper.LinhaDtoMapper;
import io.github.tinhascode.essarota.domain.model.Linha;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarLinhaUseCase {

    private static final Logger log = LoggerFactory.getLogger(CriarLinhaUseCase.class);

    private final LinhaRepository linhaRepository;
    private final LinhaDtoMapper linhaDtoMapper;

    public CriarLinhaUseCase(LinhaRepository linhaRepository, LinhaDtoMapper linhaDtoMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaDtoMapper = linhaDtoMapper;
    }

    @Transactional
    public LinhaResponse executar(CriarLinhaRequest request) {
        log.debug("Executando CriarLinhaUseCase para nome='{}', tipo='{}'", request.nome(), request.tipo());
        Linha linha = Linha.criar(request.nome(), request.tipo());
        Linha salva = linhaRepository.salvar(linha);
        log.info("Linha criada com sucesso. ID: {}, Nome: '{}', Tipo: '{}'", salva.getId(), salva.getNome(), salva.getTipo());
        return linhaDtoMapper.toResponse(salva);
    }
}
