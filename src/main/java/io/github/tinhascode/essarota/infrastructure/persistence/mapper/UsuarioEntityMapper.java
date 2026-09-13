package io.github.tinhascode.essarota.infrastructure.persistence.mapper;

import io.github.tinhascode.essarota.domain.model.Usuario;
import io.github.tinhascode.essarota.infrastructure.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {

    Usuario toDomain(UsuarioEntity entity);

    UsuarioEntity toEntity(Usuario domain);

    List<Usuario> toDomainList(List<UsuarioEntity> entities);
}
