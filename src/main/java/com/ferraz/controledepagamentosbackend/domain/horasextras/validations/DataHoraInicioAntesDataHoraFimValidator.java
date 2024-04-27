package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class DataHoraInicioAntesDataHoraFimValidator implements NovasHorasExtrasValidator {
    @Override
    public void validate(NovasHorasExtrasDTO dto) {
        if (dto.dataHoraInicio().isAfter(dto.dataHoraFim()))
            throw new ValidationException("dataHoraInicio", "A data/hora de início não pode ser posterior à data/hora de fim");
    }
}
