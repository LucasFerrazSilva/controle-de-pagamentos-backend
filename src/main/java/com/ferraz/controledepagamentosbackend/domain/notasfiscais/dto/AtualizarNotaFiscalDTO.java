package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AtualizarNotaFiscalDTO(
        @NotNull
        Long idUser,
        @NotNull
        Integer mes,
        @NotNull
        Integer ano,
        @NotNull
        @Min(0)
        BigDecimal valor
) {
}
