package io.github.tinhascode.essarota.application.usecase.linhas;

import io.github.tinhascode.essarota.application.dto.linhas.LinhaResponse;
import io.github.tinhascode.essarota.application.mapper.LinhaDtoMapper;
import io.github.tinhascode.essarota.domain.model.Linha;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListarLinhasUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarLinhasUseCase.class);

    private final LinhaRepository linhaRepository;
    private final LinhaDtoMapper linhaDtoMapper;

    public ListarLinhasUseCase(LinhaRepository linhaRepository, LinhaDtoMapper linhaDtoMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaDtoMapper = linhaDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<LinhaResponse> executar() {
        log.debug("Executando ListarLinhasUseCase");
        List<Linha> linhas = linhaRepository.listarTodas();
        log.info("Listagem de linhas retornou {} registros", linhas.size());
        return linhaDtoMapper.toResponseList(linhas);
    }
}
