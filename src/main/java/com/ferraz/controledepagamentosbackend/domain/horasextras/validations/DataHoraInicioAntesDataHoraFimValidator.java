package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataHoraInicioAntesDataHoraFimValidator implements NovasHorasExtrasValidator, AtualizarHorasExtrasValidator {

    @Override
    public void validate(NovasHorasExtrasDTO dto) {
        validate(dto.dataHoraInicio(), dto.dataHoraFim());
    }

    @Override
    public void validate(Long id, AtualizarHorasExtrasDTO dto) {
        validate(dto.dataHoraInicio(), dto.dataHoraFim());
    }

    void validate(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraInicio.isAfter(dataHoraFim))
            throw new ValidationException("dataHoraInicio", "A data/hora de início não pode ser posterior à data/hora de fim");
    }

}
