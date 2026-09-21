package io.github.tinhascode.essarota.application.usecase.trajetos;

import io.github.tinhascode.essarota.application.dto.trajetos.TrajetoResponse;
import io.github.tinhascode.essarota.application.mapper.TrajetoDtoMapper;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListarTrajetosPorUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListarTrajetosPorUsuarioUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final TrajetoDtoMapper trajetoDtoMapper;

    public ListarTrajetosPorUsuarioUseCase(TrajetoRepository trajetoRepository, TrajetoDtoMapper trajetoDtoMapper) {
        this.trajetoRepository = trajetoRepository;
        this.trajetoDtoMapper = trajetoDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<TrajetoResponse> executar(UUID usuarioId) {
        log.debug("Executando ListarTrajetosPorUsuarioUseCase para usuarioId='{}'", usuarioId);
        List<Trajeto> trajetos = trajetoRepository.listarPorUsuarioId(usuarioId);
        log.debug("Total de trajetos recuperados para usuário ID '{}': {}", usuarioId, trajetos.size());
        return trajetoDtoMapper.toResponseList(trajetos);
    }
}
