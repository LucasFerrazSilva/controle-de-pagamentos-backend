package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.domain.link.LinkStatus;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.ferraz.controledepagamentosbackend.domain.parameters.Parametros.TEMPO_EXPIRACAO_LINK;

@Component
public class LinkEstaExpiradoValidator implements AvaliarViaLinkValidator {

    private final ParametroRepository parametroRepository;

    public LinkEstaExpiradoValidator(ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public void validate(Link link) {
        if (LinkStatus.EXPIRADO.equals(link.getStatus())) {
            throw new ValidationException("status", "O link já está expirado");
        }

        parametroRepository.findById(TEMPO_EXPIRACAO_LINK.getId()).ifPresent(parametro -> {
            Long tempoExpiracaoLink = Long.parseLong(parametro.getValor().trim());
            if (link.getCreateDatetime().plusHours(tempoExpiracaoLink).isBefore(LocalDateTime.now())) {
                throw new ValidationException("createDatetime", "O link já está expirado");
            }
        });
    }
}
