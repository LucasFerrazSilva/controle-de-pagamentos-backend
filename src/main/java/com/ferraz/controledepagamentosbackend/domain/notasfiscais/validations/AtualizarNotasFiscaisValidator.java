package com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;

public interface AtualizarNotasFiscaisValidator {

    void validate(Long id, AtualizarNotaFiscalDTO dto);

}
