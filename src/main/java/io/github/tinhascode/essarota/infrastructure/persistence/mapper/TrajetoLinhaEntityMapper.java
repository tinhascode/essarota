package io.github.tinhascode.essarota.infrastructure.persistence.mapper;

import io.github.tinhascode.essarota.domain.model.TrajetoLinha;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.TrajetoLinhaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrajetoLinhaEntityMapper {

    TrajetoLinha toDomain(TrajetoLinhaEntity entity);

    TrajetoLinhaEntity toEntity(TrajetoLinha domain);

    List<TrajetoLinha> toDomainList(List<TrajetoLinhaEntity> entities);
}
