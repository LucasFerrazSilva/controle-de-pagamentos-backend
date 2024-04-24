package com.ferraz.controledepagamentosbackend.domain.horasextras.dto;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;

import java.time.LocalDateTime;

public record HorasExtrasDTO(
        Long id,
        UserDTO user,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String descricao,
        UserDTO aprovador,
        HorasExtrasStatus status
) {
    public HorasExtrasDTO(HorasExtras horasExtras) {
        this(
                horasExtras.getId(),
                new UserDTO(horasExtras.getUser()),
                horasExtras.getDataHoraInicio(),
                horasExtras.getDataHoraFim(),
                horasExtras.getDescricao(),
                new UserDTO(horasExtras.getAprovador()),
                horasExtras.getStatus()
        );
    }
}
