package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Component
public class UsuarioEAprovadorSaoDiferentesValidator implements NovasHorasExtrasValidator {

    @Override
    public void validate(NovasHorasExtrasDTO dto) {
        if (dto.idAprovador().equals(getLoggedUser().getId()))
            throw new ValidationException("idAprovador", "O aprovador não pode ser o usuario solicitante");
    }

}
