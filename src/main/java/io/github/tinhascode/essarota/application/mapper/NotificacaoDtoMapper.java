package io.github.tinhascode.essarota.application.mapper;

import io.github.tinhascode.essarota.application.dto.notificacoes.NotificacaoResponse;
import io.github.tinhascode.essarota.domain.model.Notificacao;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificacaoDtoMapper {

    NotificacaoResponse toResponse(Notificacao notificacao);

    List<NotificacaoResponse> toResponseList(List<Notificacao> notificacoes);
}
