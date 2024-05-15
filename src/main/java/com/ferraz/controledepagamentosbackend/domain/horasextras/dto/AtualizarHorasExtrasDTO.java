package com.ferraz.controledepagamentosbackend.domain.horasextras.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record AtualizarHorasExtrasDTO(
        @NotNull
        LocalDateTime dataHoraInicio,
        @NotNull
        LocalDateTime dataHoraFim,
        @Length(max = 255, message = "A descrição deve conter no máximo 255 caracteres")
        String descricao,
        @NotNull
        Long idAprovador
) {
}
