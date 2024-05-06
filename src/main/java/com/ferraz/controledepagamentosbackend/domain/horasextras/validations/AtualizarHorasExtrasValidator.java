package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;

public interface AtualizarHorasExtrasValidator {

    void validate(Long id, AtualizarHorasExtrasDTO dto);

}
