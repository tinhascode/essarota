package io.github.tinhascode.essarota.application.usecase.trajetos;

import io.github.tinhascode.essarota.application.dto.trajetos.AtualizarTrajetoRequest;
import io.github.tinhascode.essarota.application.dto.trajetos.TrajetoResponse;
import io.github.tinhascode.essarota.application.mapper.TrajetoDtoMapper;
import io.github.tinhascode.essarota.domain.exception.AcessoNegadoAoTrajetoException;
import io.github.tinhascode.essarota.domain.exception.TrajetoNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import io.github.tinhascode.essarota.domain.service.RouteCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AtualizarTrajetoUseCase {

    private static final Logger log = LoggerFactory.getLogger(AtualizarTrajetoUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final TrajetoDtoMapper trajetoDtoMapper;
    private final RouteCalculatorService routeCalculatorService;

    public AtualizarTrajetoUseCase(
            TrajetoRepository trajetoRepository,
            TrajetoDtoMapper trajetoDtoMapper,
            RouteCalculatorService routeCalculatorService
    ) {
        this.trajetoRepository = trajetoRepository;
        this.trajetoDtoMapper = trajetoDtoMapper;
        this.routeCalculatorService = routeCalculatorService;
    }

    @Transactional
    public TrajetoResponse executar(UUID trajetoId, UUID usuarioAutenticadoId, AtualizarTrajetoRequest request) {
        log.debug("Executando AtualizarTrajetoUseCase para trajetoId='{}', usuarioId='{}'", trajetoId, usuarioAutenticadoId);

        Trajeto trajeto = trajetoRepository.buscarPorId(trajetoId)
                .orElseThrow(() -> {
                    log.warn("Falha ao atualizar trajeto: ID '{}' não encontrado", trajetoId);
                    return new TrajetoNaoEncontradoException(trajetoId);
                });

        if (!trajeto.pertenceAoUsuario(usuarioAutenticadoId)) {
            log.warn("Acesso negado para atualizar trajeto: Trajeto ID '{}' não pertence ao usuário ID '{}'",
                    trajetoId, usuarioAutenticadoId);
            throw new AcessoNegadoAoTrajetoException(trajetoId);
        }

        Integer tempoEstimado = request.tempoEstimadoMinutos();
        if (tempoEstimado == null) {
            log.debug("Tempo estimado omitido. Recalculando rota de '{}' para '{}'...", request.origem(), request.destino());
            tempoEstimado = routeCalculatorService.calcularTempoEstimadoMinutos(request.origem(), request.destino());
            log.debug("Novo tempo estimado calculado: {} minutos", tempoEstimado);
        }

        trajeto.atualizar(request.origem(), request.destino(), tempoEstimado);
        Trajeto atualizado = trajetoRepository.salvar(trajeto);

        log.info("Trajeto ID '{}' atualizado com sucesso no repositório", atualizado.getId());
        return trajetoDtoMapper.toResponse(atualizado);
    }
}
