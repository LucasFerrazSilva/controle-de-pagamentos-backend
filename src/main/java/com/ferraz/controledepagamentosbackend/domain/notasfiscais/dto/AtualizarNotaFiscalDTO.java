package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;

import java.math.BigDecimal;

public record AtualizarNotaFiscalDTO(
        Long idUser,
        Integer mes,
        Integer ano,
        BigDecimal valor,
        String filePath
) {
}
