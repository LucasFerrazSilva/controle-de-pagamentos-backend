package com.ferraz.controledepagamentosbackend.domain.user;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ferraz.controledepagamentosbackend.infra.email.EmailDTO;
import com.ferraz.controledepagamentosbackend.infra.email.EmailService;

@Component
public class EnviarCredenciaisEmail {
	private TemplateEngine templateEngine;
	
	@Value("${application_sender}")
    private String applicationSender;
	
	private EmailService emailService;
	
	public EnviarCredenciaisEmail(TemplateEngine templateEngine, EmailService emailService) {
		this.templateEngine = templateEngine;
		this.emailService = emailService;
	}
	
	public void enviarCredenciaisEmail(User user, String randomPassword) {
		String subject = "Senha Gerada Controle de Pagamentos";
		String html = buildHtml(user, randomPassword);
		EmailDTO emailDTO = new EmailDTO(applicationSender, user.getEmail(), subject, html);
		
		new Thread(() -> this.emailService.sendMailSMTP(emailDTO)).start();
	}
	
	
	private String buildHtml(User user, String randomPassword) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Map<String, Object> variables = new HashMap<>();
        variables.put("nomeUsuario", user.getNome());
        variables.put("emailUsuario", user.getEmail());
        variables.put("senhaUsuario", randomPassword);
        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process("envio-senha-gerada.html", context);
    }
	
}
