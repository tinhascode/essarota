package io.github.tinhascode.essarota.infrastructure.persistence.mapper;

import io.github.tinhascode.essarota.domain.model.Trajeto;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.TrajetoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrajetoEntityMapper {

    Trajeto toDomain(TrajetoEntity entity);

    TrajetoEntity toEntity(Trajeto domain);

    List<Trajeto> toDomainList(List<TrajetoEntity> entities);
}
