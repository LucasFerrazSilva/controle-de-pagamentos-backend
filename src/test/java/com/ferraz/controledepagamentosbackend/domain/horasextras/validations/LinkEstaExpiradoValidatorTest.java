package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.domain.link.LinkStatus;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LinkEstaExpiradoValidatorTest {

    @Test
    void test() {
        // Given
        LinkEstaExpiradoValidator validator = new LinkEstaExpiradoValidator(null);
        Link link = new Link();
        link.setStatus(LinkStatus.EXPIRADO);

        // When / Then
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(link));
    }

}