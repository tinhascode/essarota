package io.github.tinhascode.essarota.infrastructure.persistence.mapper;

import io.github.tinhascode.essarota.domain.model.Alerta;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.AlertaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertaEntityMapper {

    Alerta toDomain(AlertaEntity entity);

    AlertaEntity toEntity(Alerta domain);

    List<Alerta> toDomainList(List<AlertaEntity> entities);
}
