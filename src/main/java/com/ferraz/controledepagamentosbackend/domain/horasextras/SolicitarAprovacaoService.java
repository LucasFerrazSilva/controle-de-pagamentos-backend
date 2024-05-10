package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.link.AcaoLink;
import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.domain.link.LinkService;
import com.ferraz.controledepagamentosbackend.infra.email.EmailDTO;
import com.ferraz.controledepagamentosbackend.infra.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class SolicitarAprovacaoService {

    @Value("${application_sender}")
    private String applicationSender;

    private final LinkService linkService;
    private SpringTemplateEngine templateEngine;
    private EmailService emailService;

    public SolicitarAprovacaoService(LinkService linkService, SpringTemplateEngine templateEngine, EmailService emailService) {
        this.linkService = linkService;
        this.templateEngine = templateEngine;
        this.emailService = emailService;
    }

    public void solicitar(HorasExtras horasExtras) {
        Link aprovar = linkService.criar(AcaoLink.APROVAR, horasExtras);
        Link recusar = linkService.criar(AcaoLink.RECUSAR, horasExtras);
        String baseUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        String linkAprovar = baseUrl + "/horas-extras/avaliar-via-link/" + aprovar.getId();
        String linkRecusar = baseUrl + "/horas-extras/avaliar-via-link/" + recusar.getId();

        String subject = "Solicitação de aprovação de horas extras";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Map<String, Object> variables = new HashMap<>();
        variables.put("nomeAprovador", horasExtras.getAprovador().getNome());
        variables.put("nomeSolicitante", horasExtras.getUser().getNome());
        variables.put("inicioHorasExtras", horasExtras.getDataHoraInicio().format(formatter));
        variables.put("fimHorasExtras", horasExtras.getDataHoraFim().format(formatter));
        variables.put("linkAprovar", linkAprovar);
        variables.put("linkRecusar", linkRecusar);
        variables.put("descricao", horasExtras.getDescricao());
        Context context = new Context();
        context.setVariables(variables);
        String html = templateEngine.process("solicitacao-de-analise.html", context);

        EmailDTO emailDTO = new EmailDTO(applicationSender, horasExtras.getAprovador().getEmail(), subject, html);

        this.emailService.sendMailSMTP(emailDTO);

    }

}
