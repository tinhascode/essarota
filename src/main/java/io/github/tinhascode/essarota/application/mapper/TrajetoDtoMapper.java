package io.github.tinhascode.essarota.application.mapper;

import io.github.tinhascode.essarota.application.dto.trajetos.TrajetoResponse;
import io.github.tinhascode.essarota.domain.model.Trajeto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrajetoDtoMapper {

    TrajetoResponse toResponse(Trajeto trajeto);

    List<TrajetoResponse> toResponseList(List<Trajeto> trajetos);
}
