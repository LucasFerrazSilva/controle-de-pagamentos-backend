package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NovaNotaFiscalDTO(
        @NotNull
        Long idUser,
        @NotNull
        Integer mes,
        @NotNull
        Integer ano,
        @NotNull
        @Min(0)
        BigDecimal valor,
        String filePath
) {
}
