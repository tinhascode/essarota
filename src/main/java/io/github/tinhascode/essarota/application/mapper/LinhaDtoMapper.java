package io.github.tinhascode.essarota.application.mapper;

import io.github.tinhascode.essarota.application.dto.linhas.LinhaResponse;
import io.github.tinhascode.essarota.domain.model.Linha;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LinhaDtoMapper {

    LinhaResponse toResponse(Linha linha);

    List<LinhaResponse> toResponseList(List<Linha> linhas);
}
