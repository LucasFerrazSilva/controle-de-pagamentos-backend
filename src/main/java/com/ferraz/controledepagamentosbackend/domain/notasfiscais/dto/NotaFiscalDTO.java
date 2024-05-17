package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;

import java.math.BigDecimal;

public record NotaFiscalDTO(
        Long id,
        UserDTO userDTO,
        Integer mes,
        Integer ano,
        BigDecimal valor,
        String filePath,
        NotaFiscalStatus status
        ) {
    public NotaFiscalDTO(NotaFiscal notaFiscal){
        this (
                notaFiscal.getId(),
                new UserDTO(notaFiscal.getUser()),
                notaFiscal.getMes(),
                notaFiscal.getAno(),
                notaFiscal.getValor(),
                notaFiscal.getFilePath(),
                notaFiscal.getStatus()

        );

    }
}
