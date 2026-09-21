package io.github.tinhascode.essarota.application.usecase.trajetos;

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
public class DeletarTrajetoUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeletarTrajetoUseCase.class);

    private final TrajetoRepository trajetoRepository;

    public DeletarTrajetoUseCase(TrajetoRepository trajetoRepository) {
        this.trajetoRepository = trajetoRepository;
    }

    @Transactional
    public void executar(UUID trajetoId, UUID usuarioAutenticadoId) {
        log.debug("Executando DeletarTrajetoUseCase para trajetoId='{}', usuarioId='{}'", trajetoId, usuarioAutenticadoId);

        Trajeto trajeto = trajetoRepository.buscarPorId(trajetoId)
                .orElseThrow(() -> {
                    log.warn("Falha ao deletar: Trajeto ID '{}' não encontrado", trajetoId);
                    return new TrajetoNaoEncontradoException(trajetoId);
                });

        if (!trajeto.pertenceAoUsuario(usuarioAutenticadoId)) {
            log.warn("Acesso negado para deletar: Trajeto ID '{}' não pertence ao usuário ID '{}'", trajetoId, usuarioAutenticadoId);
            throw new AcessoNegadoAoTrajetoException(trajetoId);
        }

        trajetoRepository.deletarPorId(trajetoId);
        log.info("Trajeto ID '{}' removido com sucesso do repositório", trajetoId);
    }
}
