package io.github.tinhascode.essarota.application.usecase.trajetoslinhas;

import io.github.tinhascode.essarota.application.dto.trajetoslinhas.TrajetoLinhaResponse;
import io.github.tinhascode.essarota.application.mapper.TrajetoLinhaDtoMapper;
import io.github.tinhascode.essarota.domain.exception.AcessoNegadoAoTrajetoException;
import io.github.tinhascode.essarota.domain.exception.TrajetoNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.model.TrajetoLinha;
import io.github.tinhascode.essarota.domain.repository.TrajetoLinhaRepository;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListarLinhasDoTrajetoUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarLinhasDoTrajetoUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final TrajetoLinhaRepository trajetoLinhaRepository;
    private final TrajetoLinhaDtoMapper trajetoLinhaDtoMapper;

    public ListarLinhasDoTrajetoUseCase(
            TrajetoRepository trajetoRepository,
            TrajetoLinhaRepository trajetoLinhaRepository,
            TrajetoLinhaDtoMapper trajetoLinhaDtoMapper
    ) {
        this.trajetoRepository = trajetoRepository;
        this.trajetoLinhaRepository = trajetoLinhaRepository;
        this.trajetoLinhaDtoMapper = trajetoLinhaDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<TrajetoLinhaResponse> executar(UUID trajetoId, UUID usuarioAutenticadoId) {
        log.debug("Executando ListarLinhasDoTrajetoUseCase para Trajeto: {}, Usuario: {}", trajetoId, usuarioAutenticadoId);

        Trajeto trajeto = trajetoRepository.buscarPorId(trajetoId)
                .orElseThrow(() -> new TrajetoNaoEncontradoException(trajetoId));

        if (!trajeto.pertenceAoUsuario(usuarioAutenticadoId)) {
            throw new AcessoNegadoAoTrajetoException(trajetoId);
        }

        List<TrajetoLinha> lista = trajetoLinhaRepository.listarPorTrajetoId(trajetoId);
        log.info("Listagem retornou {} linhas associadas ao trajeto {}", lista.size(), trajetoId);
        return trajetoLinhaDtoMapper.toResponseList(lista);
    }
}
