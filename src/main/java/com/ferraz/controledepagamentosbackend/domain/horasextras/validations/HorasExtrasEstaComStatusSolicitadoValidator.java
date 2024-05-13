package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class HorasExtrasEstaComStatusSolicitadoValidator implements AvaliarViaLinkValidator {
    @Override
    public void validate(Link link) {
        if (!HorasExtrasStatus.SOLICITADO.equals(link.getHorasExtras().getStatus())) {
            throw new ValidationException("horasExtras", "O registro não está com status SOLICITADO");
        }
    }
}
