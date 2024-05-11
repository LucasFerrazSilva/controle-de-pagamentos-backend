package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;

import java.math.BigDecimal;

public record AtualizarNotaFiscalDTO(
        Long idUser,
        Integer mes,
        Integer ano,
        BigDecimal valor,
        String filePath,
        NotaFiscalStatus notaFiscalStatus
) {
}
