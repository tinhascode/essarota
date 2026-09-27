package io.github.tinhascode.essarota.application.mapper;

import io.github.tinhascode.essarota.application.dto.trajetoslinhas.TrajetoLinhaResponse;
import io.github.tinhascode.essarota.domain.model.TrajetoLinha;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrajetoLinhaDtoMapper {

    TrajetoLinhaResponse toResponse(TrajetoLinha trajetoLinha);

    List<TrajetoLinhaResponse> toResponseList(List<TrajetoLinha> trajetosLinhas);
}
