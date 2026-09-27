package io.github.tinhascode.essarota.infrastructure.persistence.mapper;

import io.github.tinhascode.essarota.domain.model.Linha;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.LinhaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LinhaEntityMapper {

    Linha toDomain(LinhaEntity entity);

    LinhaEntity toEntity(Linha domain);

    List<Linha> toDomainList(List<LinhaEntity> entities);
}
