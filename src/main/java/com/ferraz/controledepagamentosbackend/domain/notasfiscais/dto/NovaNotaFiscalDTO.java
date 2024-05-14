package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;

import java.math.BigDecimal;

public record NovaNotaFiscalDTO(
        Long idUser,
        Integer mes,
        Integer ano,
        BigDecimal valor,
        String filePath,
        NotaFiscalStatus status
) {
}
