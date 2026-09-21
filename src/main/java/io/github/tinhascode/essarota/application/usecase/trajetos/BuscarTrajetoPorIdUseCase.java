package io.github.tinhascode.essarota.application.usecase.trajetos;

import io.github.tinhascode.essarota.application.dto.trajetos.TrajetoResponse;
import io.github.tinhascode.essarota.application.mapper.TrajetoDtoMapper;
import io.github.tinhascode.essarota.domain.exception.AcessoNegadoAoTrajetoException;
import io.github.tinhascode.essarota.domain.exception.TrajetoNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarTrajetoPorIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(BuscarTrajetoPorIdUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final TrajetoDtoMapper trajetoDtoMapper;

    public BuscarTrajetoPorIdUseCase(TrajetoRepository trajetoRepository, TrajetoDtoMapper trajetoDtoMapper) {
        this.trajetoRepository = trajetoRepository;
        this.trajetoDtoMapper = trajetoDtoMapper;
    }

    @Transactional(readOnly = true)
    public TrajetoResponse executar(UUID trajetoId, UUID usuarioAutenticadoId) {
        log.debug("Executando BuscarTrajetoPorIdUseCase para trajetoId='{}', usuarioId='{}'", trajetoId, usuarioAutenticadoId);

        Trajeto trajeto = trajetoRepository.buscarPorId(trajetoId)
                .orElseThrow(() -> {
                    log.warn("Trajeto não encontrado com ID: {}", trajetoId);
                    return new TrajetoNaoEncontradoException(trajetoId);
                });

        if (!trajeto.pertenceAoUsuario(usuarioAutenticadoId)) {
            log.warn("Acesso negado: Trajeto ID '{}' não pertence ao usuário ID '{}'", trajetoId, usuarioAutenticadoId);
            throw new AcessoNegadoAoTrajetoException(trajetoId);
        }

        return trajetoDtoMapper.toResponse(trajeto);
    }
}
