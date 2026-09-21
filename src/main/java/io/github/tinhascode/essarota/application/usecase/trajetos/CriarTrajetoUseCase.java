package io.github.tinhascode.essarota.application.usecase.trajetos;

import io.github.tinhascode.essarota.application.dto.trajetos.CriarTrajetoRequest;
import io.github.tinhascode.essarota.application.dto.trajetos.TrajetoResponse;
import io.github.tinhascode.essarota.application.mapper.TrajetoDtoMapper;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import io.github.tinhascode.essarota.domain.service.RouteCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CriarTrajetoUseCase {

    private static final Logger log = LoggerFactory.getLogger(CriarTrajetoUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final TrajetoDtoMapper trajetoDtoMapper;
    private final RouteCalculatorService routeCalculatorService;

    public CriarTrajetoUseCase(
            TrajetoRepository trajetoRepository,
            TrajetoDtoMapper trajetoDtoMapper,
            RouteCalculatorService routeCalculatorService
    ) {
        this.trajetoRepository = trajetoRepository;
        this.trajetoDtoMapper = trajetoDtoMapper;
        this.routeCalculatorService = routeCalculatorService;
    }

    @Transactional
    public TrajetoResponse executar(UUID usuarioId, CriarTrajetoRequest request) {
        log.debug("Executando CriarTrajetoUseCase para usuarioId='{}', origem='{}', destino='{}'",
                usuarioId, request.origem(), request.destino());

        Integer tempoEstimado = request.tempoEstimadoMinutos();
        if (tempoEstimado == null) {
            log.debug("Tempo estimado não fornecido. Calculando via RouteCalculatorService...");
            tempoEstimado = routeCalculatorService.calcularTempoEstimadoMinutos(request.origem(), request.destino());
            log.debug("Tempo estimado calculado: {} minutos", tempoEstimado);
        }

        Trajeto trajeto = Trajeto.criar(usuarioId, request.origem(), request.destino(), tempoEstimado);
        Trajeto salvo = trajetoRepository.salvar(trajeto);

        log.info("Trajeto criado com sucesso. ID: {}, UsuarioID: {}, Tempo: {} min", salvo.getId(), salvo.getUsuarioId(), salvo.getTempoEstimadoMinutos());
        return trajetoDtoMapper.toResponse(salvo);
    }
}
