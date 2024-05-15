package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.link.AcaoLink;
import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.domain.link.LinkService;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametros;
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
    private final SpringTemplateEngine templateEngine;
    private final EmailService emailService;
    private final ParametroRepository parametroRepository;

    public SolicitarAprovacaoService(LinkService linkService, SpringTemplateEngine templateEngine, EmailService emailService, ParametroRepository parametroRepository) {
        this.linkService = linkService;
        this.templateEngine = templateEngine;
        this.emailService = emailService;
        this.parametroRepository = parametroRepository;
    }

    public void solicitar(HorasExtras horasExtras) {
        linkService.inativarLinks(horasExtras);

        String linkAprovar = createAndBuildLink(AcaoLink.APROVAR, horasExtras);
        String linkRecusar = createAndBuildLink(AcaoLink.RECUSAR, horasExtras);

        if (!deveEnviarEmailDeSolicitacaoDeAvaliacao())
            return;

        String html = buildHtml(horasExtras, linkAprovar, linkRecusar);
        String subject = "Solicitação de aprovação de horas extras";
        EmailDTO emailDTO = new EmailDTO(applicationSender, horasExtras.getAprovador().getEmail(), subject, html);
        this.emailService.sendMailSMTP(emailDTO);
    }


    private String buildHtml(HorasExtras horasExtras, String linkAprovar, String linkRecusar) {
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

        return templateEngine.process("solicitacao-de-analise.html", context);
    }

    private String createAndBuildLink(AcaoLink acaoLink, HorasExtras horasExtras) {
        Link link = linkService.criar(acaoLink, horasExtras);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return "%s/horas-extras/avaliar-via-link/%s".formatted(baseUrl, link.getId());
    }

    private boolean deveEnviarEmailDeSolicitacaoDeAvaliacao() {
        return parametroRepository
                        .findById(Parametros.DEVE_ENVIAR_EMAIL_AVALIACAO.getId())
                        .map(parametro -> "S".equals(parametro.getValor()))
                        .orElse(false);
    }

}
