package io.github.tinhascode.essarota.infrastructure.routing;

import io.github.tinhascode.essarota.domain.service.RouteCalculatorService;
import org.springframework.stereotype.Service;

@Service
public class DefaultRouteCalculatorImpl implements RouteCalculatorService {

    private static final int TEMPO_BASE_MINUTOS = 20;
    private static final int FATOR_CALCULO = 35;

    @Override
    public Integer calcularTempoEstimadoMinutos(String origem, String destino) {
        if (origem == null || destino == null) {
            return TEMPO_BASE_MINUTOS;
        }

        // Estimativa inicial determinística (pronta para plugar OpenTripPlanner / GTFS)
        int seed = Math.abs((origem.trim() + "->" + destino.trim()).hashCode());
        int variacao = (seed % FATOR_CALCULO) + 10;

        return TEMPO_BASE_MINUTOS + variacao;
    }
}
