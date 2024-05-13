package com.ferraz.controledepagamentosbackend.infra.email;

public record EmailDTO(
        String from,
        String to,
        String subject,
        String html
) {
}
