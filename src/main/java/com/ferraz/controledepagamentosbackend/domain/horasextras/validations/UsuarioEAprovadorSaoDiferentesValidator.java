package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Component
public class UsuarioEAprovadorSaoDiferentesValidator implements NovasHorasExtrasValidator, AtualizarHorasExtrasValidator {

    @Override
    public void validate(NovasHorasExtrasDTO dto) {
        validate(dto.idAprovador());
    }

    @Override
    public void validate(Long id, AtualizarHorasExtrasDTO dto) {
        validate(dto.idAprovador());
    }

    private void validate(Long idAprovador) {
        if (idAprovador.equals(getLoggedUser().getId()))
            throw new ValidationException("idAprovador", "O aprovador não pode ser o usuario solicitante");

    }

}
