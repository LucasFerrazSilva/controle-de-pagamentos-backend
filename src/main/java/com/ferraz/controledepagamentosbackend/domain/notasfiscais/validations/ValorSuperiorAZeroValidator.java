package com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;

import java.math.BigDecimal;

public class ValorSuperiorAZeroValidator implements NovasNotasFiscaisValidator,AtualizarNotasFiscaisValidator{
    @Override
    public void validate(Long id, AtualizarNotaFiscalDTO dto) {
        validate(dto.valor());
    }

    @Override
    public void validate(NovaNotaFiscalDTO dto) {
        validate(dto.valor());
    }

    void validate (BigDecimal valor){
        if (valor.intValue() <= 0){
            throw new ValidationException("valor", "O valor tem que ser maior do que zero");
        }
    }
}
