package com.ferraz.controledepagamentosbackend.domain.horasextras.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NovasHorasExtrasDTO(
        @NotNull
        LocalDateTime dataHoraInicio,
        @NotNull
        LocalDateTime dataHoraFim,
        String descricao,
        @NotNull
        Long idAprovador
) {
}
