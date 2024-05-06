package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataEHoraEstaoLivresValidator implements NovasHorasExtrasValidator, AtualizarHorasExtrasValidator {

    private static final List<HorasExtrasStatus> STATUS_ATIVOS = List.of(HorasExtrasStatus.SOLICITADO, HorasExtrasStatus.APROVADO);

    private HorasExtrasRepository repository;

    public DataEHoraEstaoLivresValidator(HorasExtrasRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Long id, AtualizarHorasExtrasDTO dto) {
        List<HorasExtras> horasExtrasAtivas = repository.findByIdNotAndUserAndStatusIn(id, AuthenticationService.getLoggedUser(), STATUS_ATIVOS);
        validate(horasExtrasAtivas, dto.dataHoraInicio(), dto.dataHoraFim());
    }

    @Override
    public void validate(NovasHorasExtrasDTO dto) {
        List<HorasExtras> horasExtrasAtivas = repository.findByUserAndStatusIn(AuthenticationService.getLoggedUser(), STATUS_ATIVOS);
        validate(horasExtrasAtivas, dto.dataHoraInicio(), dto.dataHoraFim());
    }

    private void validate(List<HorasExtras> horasExtrasAtivas, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        horasExtrasAtivas.forEach(horasExtras -> {
            boolean dataHoraInicioOcupada = dataHoraNoRange(dataHoraInicio, horasExtras.getDataHoraInicio(), horasExtras.getDataHoraFim());
            boolean dataHoraFimOcupada = dataHoraNoRange(dataHoraFim, horasExtras.getDataHoraInicio(), horasExtras.getDataHoraFim());
            boolean dataHoraRegistroInicioOcupada = dataHoraNoRange(horasExtras.getDataHoraInicio(), dataHoraInicio, dataHoraFim);
            boolean dataHoraRegistroFimOcupada = dataHoraNoRange(horasExtras.getDataHoraFim(), dataHoraFim, dataHoraInicio);

            if (dataHoraInicioOcupada || dataHoraFimOcupada || dataHoraRegistroInicioOcupada || dataHoraRegistroFimOcupada)
                throw new ValidationException("dataHora", "O range de data/hora já está sendo ocupado em outro registro ativo");
        });
    }

    private boolean dataHoraNoRange(LocalDateTime dataHora, LocalDateTime inicioRange, LocalDateTime fimRange) {
        return (inicioRange.equals(dataHora) || inicioRange.isBefore(dataHora)) && (fimRange.equals(dataHora) || fimRange.isAfter(dataHora));
    }

}
