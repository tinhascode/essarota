package io.github.tinhascode.essarota.application.usecase.trajetoslinhas;

import io.github.tinhascode.essarota.domain.exception.AcessoNegadoAoTrajetoException;
import io.github.tinhascode.essarota.domain.exception.TrajetoLinhaNaoEncontradaException;
import io.github.tinhascode.essarota.domain.exception.TrajetoNaoEncontradoException;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.domain.repository.TrajetoLinhaRepository;
import io.github.tinhascode.essarota.domain.repository.TrajetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverLinhaDoTrajetoUseCase {

    private static final Logger log = LoggerFactory.getLogger(RemoverLinhaDoTrajetoUseCase.class);

    private final TrajetoRepository trajetoRepository;
    private final TrajetoLinhaRepository trajetoLinhaRepository;

    public RemoverLinhaDoTrajetoUseCase(
            TrajetoRepository trajetoRepository,
            TrajetoLinhaRepository trajetoLinhaRepository
    ) {
        this.trajetoRepository = trajetoRepository;
        this.trajetoLinhaRepository = trajetoLinhaRepository;
    }

    @Transactional
    public void executar(UUID trajetoId, UUID linhaId, UUID usuarioAutenticadoId) {
        log.debug("Executando RemoverLinhaDoTrajetoUseCase. Trajeto: {}, Linha: {}, Usuario: {}",
                trajetoId, linhaId, usuarioAutenticadoId);

        Trajeto trajeto = trajetoRepository.buscarPorId(trajetoId)
                .orElseThrow(() -> new TrajetoNaoEncontradoException(trajetoId));

        if (!trajeto.pertenceAoUsuario(usuarioAutenticadoId)) {
            throw new AcessoNegadoAoTrajetoException(trajetoId);
        }

        if (!trajetoLinhaRepository.existePorTrajetoIdELinhaId(trajetoId, linhaId)) {
            throw new TrajetoLinhaNaoEncontradaException(trajetoId, linhaId);
        }

        trajetoLinhaRepository.deletarPorTrajetoIdELinhaId(trajetoId, linhaId);
        log.info("Associação entre trajeto {} e linha {} removida com sucesso", trajetoId, linhaId);
    }
}
