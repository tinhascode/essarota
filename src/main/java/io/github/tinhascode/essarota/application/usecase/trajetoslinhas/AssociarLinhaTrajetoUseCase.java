package io.github.tinhascode.essarota.application.usecase.trajetoslinhas;

import io.github.tinhascode.essarota.application.dto.trajetoslinhas.AssociarLinhaTrajetoRequest;
import io.github.tinhascode.essarota.application.dto.trajetoslinhas.TrajetoLinhaResponse;
import io.github.tinhascode.essarota.application.mapper.TrajetoLinhaDtoMapper;
import io.github.tinhascode.essarota.domain.exception.AcessoNegadoAoTrajetoException;
import io.github.tinhascode.essarota.domain.exception.LinhaNaoEncontradaException;
import io.github.tinhascode.essarota.domain.exception.TrajetoNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.model.TrajetoLinha;
import io.github.tinhascode.essarota.domain.repository.LinhaRepository;
import io.github.tinhascode.essarota.domain.repository.TrajetoLinhaRepository;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AssociarLinhaTrajetoUseCase {

    private static final Logger log = LoggerFactory.getLogger(AssociarLinhaTrajetoUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final LinhaRepository linhaRepository;
    private final TrajetoLinhaRepository trajetoLinhaRepository;
    private final TrajetoLinhaDtoMapper trajetoLinhaDtoMapper;

    public AssociarLinhaTrajetoUseCase(
            TrajetoRepository trajetoRepository,
            LinhaRepository linhaRepository,
            TrajetoLinhaRepository trajetoLinhaRepository,
            TrajetoLinhaDtoMapper trajetoLinhaDtoMapper
    ) {
        this.trajetoRepository = trajetoRepository;
        this.linhaRepository = linhaRepository;
        this.trajetoLinhaRepository = trajetoLinhaRepository;
        this.trajetoLinhaDtoMapper = trajetoLinhaDtoMapper;
    }

    @Transactional
    public TrajetoLinhaResponse executar(UUID trajetoId, UUID usuarioAutenticadoId, AssociarLinhaTrajetoRequest request) {
        log.debug("Executando AssociarLinhaTrajetoUseCase. Trajeto: {}, Linha: {}, Usuario: {}",
                trajetoId, request.linhaId(), usuarioAutenticadoId);

        Trajeto trajeto = trajetoRepository.buscarPorId(trajetoId)
                .orElseThrow(() -> new TrajetoNaoEncontradoException(trajetoId));

        if (!trajeto.pertenceAoUsuario(usuarioAutenticadoId)) {
            throw new AcessoNegadoAoTrajetoException(trajetoId);
        }

        if (!linhaRepository.existePorId(request.linhaId())) {
            throw new LinhaNaoEncontradaException(request.linhaId());
        }

        TrajetoLinha trajetoLinha = trajetoLinhaRepository.buscarPorTrajetoIdELinhaId(trajetoId, request.linhaId())
                .map(existente -> {
                    existente.atualizarOrdem(request.ordem());
                    return existente;
                })
                .orElseGet(() -> TrajetoLinha.criar(trajetoId, request.linhaId(), request.ordem()));

        TrajetoLinha salvo = trajetoLinhaRepository.salvar(trajetoLinha);
        log.info("Linha {} associada ao trajeto {} com ordem {}", request.linhaId(), trajetoId, salvo.getOrdem());
        return trajetoLinhaDtoMapper.toResponse(salvo);
    }
}
