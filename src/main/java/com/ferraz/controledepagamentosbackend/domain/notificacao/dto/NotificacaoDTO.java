package com.ferraz.controledepagamentosbackend.domain.notificacao.dto;

import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;

public record NotificacaoDTO(
        Long id,
        UserDTO userDTO,
        String descricao,
        String path,
        NotificacaoStatus status
) {
    public NotificacaoDTO(Notificacao notificacao) {
        this(
                notificacao.getId(),
                new UserDTO(notificacao.getUser()),
                notificacao.getDescricao(),
                notificacao.getPath(),
                notificacao.getStatus()
        );
    }
}
