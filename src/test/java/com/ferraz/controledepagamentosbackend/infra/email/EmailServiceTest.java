package com.ferraz.controledepagamentosbackend.infra.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceTest {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;


    @Test
    @DisplayName("Deve enviar email quando passar dados validos")
    void testSendMailSMTP() {
        // Given
        EmailDTO emailDTO = new EmailDTO("teste@teste.com", "to@mail.com", "Teste", "HTML teste");

        // When
        boolean sent = emailService.sendMailSMTP(emailDTO);

        // Then
        assertThat(sent).isTrue();
    }
    @Test
    @DisplayName("Deve lançar exception ")
    void testSendMailSMTP_Exception() {
        // Given
        EmailDTO emailDTO = new EmailDTO(null, null, null, null);

        // When
        boolean sent = emailService.sendMailSMTP(emailDTO);

        // Then
        assertThat(sent).isFalse();

    }

}