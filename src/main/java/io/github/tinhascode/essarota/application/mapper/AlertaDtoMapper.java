package io.github.tinhascode.essarota.application.mapper;

import io.github.tinhascode.essarota.application.dto.alertas.AlertaResponse;
import io.github.tinhascode.essarota.domain.model.Alerta;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertaDtoMapper {

    AlertaResponse toResponse(Alerta alerta);

    List<AlertaResponse> toResponseList(List<Alerta> alertas);
}
