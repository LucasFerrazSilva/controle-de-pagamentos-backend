package com.ferraz.controledepagamentosbackend.infra.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmailDTOTest {

    @Test
    void test() {
        // Given
        String from = "from";
        String to = "to";
        String subject = "subject";
        String html = "html";

        // When
        EmailDTO emailDTO = new EmailDTO(from, to, subject, html);

        // Then
        assertThat(emailDTO.from()).isEqualTo(from);
        assertThat(emailDTO.to()).isEqualTo(to);
        assertThat(emailDTO.subject()).isEqualTo(subject);
        assertThat(emailDTO.html()).isEqualTo(html);
    }

}