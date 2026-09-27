package io.github.tinhascode.essarota.infrastructure.persistence.mapper;

import io.github.tinhascode.essarota.domain.model.Notificacao;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.NotificacaoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificacaoEntityMapper {

    Notificacao toDomain(NotificacaoEntity entity);

    NotificacaoEntity toEntity(Notificacao domain);

    List<Notificacao> toDomainList(List<NotificacaoEntity> entities);
}
