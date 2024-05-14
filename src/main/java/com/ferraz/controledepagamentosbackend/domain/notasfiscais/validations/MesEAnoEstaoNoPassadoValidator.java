package com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;

import java.time.LocalDateTime;

public class MesEAnoEstaoNoPassadoValidator implements AtualizarNotasFiscaisValidator, NovasNotasFiscaisValidator{

    @Override
    public void validate(Long id, AtualizarNotaFiscalDTO dto) { validate(dto.mes(), dto.ano()); }

    @Override
    public void validate(NovaNotaFiscalDTO dto) { validate(dto.mes(), dto.ano()); }

    public void validate(Integer mes, Integer ano){
        if(LocalDateTime.now().getMonthValue() < mes && LocalDateTime.now().getYear() < ano){
            throw new ValidationException("mes", "O mês ultrapassa o mês atual");
        }

        if(LocalDateTime.now().getYear() < ano){
            throw new ValidationException("ano", "O ano ultrapassa o ano atual");
        }

    }
}
