package io.github.tinhascode.essarota.application.mapper;

import io.github.tinhascode.essarota.application.dto.usuarios.CriarUsuarioRequest;
import io.github.tinhascode.essarota.application.dto.usuarios.UsuarioResponse;
import io.github.tinhascode.essarota.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    @Mapping(target = "id", ignore = true)
    Usuario toDomain(CriarUsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);

    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);
}
