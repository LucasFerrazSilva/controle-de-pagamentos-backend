package com.ferraz.controledepagamentosbackend.domain.emails;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametros;
import com.ferraz.controledepagamentosbackend.infra.email.EmailDTO;
import com.ferraz.controledepagamentosbackend.infra.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificarSolicitacaoDeNotaFiscalService {

    @Value("${application_sender}")
    private String applicationSender;
    @Value("${frontend.url}")
    private String linkAplicacao;
    private final SpringTemplateEngine templateEngine;
    private final EmailService emailService;
    private final ParametroRepository parametroRepository;

    public NotificarSolicitacaoDeNotaFiscalService(SpringTemplateEngine templateEngine, EmailService emailService, ParametroRepository parametroRepository) {
        this.templateEngine = templateEngine;
        this.emailService = emailService;
        this.parametroRepository = parametroRepository;
    }

    public void enviar(NotaFiscal notaFiscal) {
        if (!deveEnviarEmailDeSolicitacaoDeNotaFiscal())
            return;

        String html = buildHtml(notaFiscal);
        String subject = "Solicitação de Nota Fiscal";
        EmailDTO emailDTO = new EmailDTO(applicationSender, notaFiscal.getUser().getEmail(), subject, html);
        new Thread(() -> this.emailService.sendMailSMTP(emailDTO)).start();
    }

    private String buildHtml(NotaFiscal notaFiscal) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("nomePrestador", notaFiscal.getUser().getNome());
        variables.put("valorTotal", notaFiscal.getValor().toString().replace(".", ","));
        variables.put("mes", notaFiscal.getMes());
        variables.put("ano", notaFiscal.getAno());
        variables.put("linkAplicacao", linkAplicacao);

        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process("solicitacao-de-nota-fiscal.html", context);
    }

    private boolean deveEnviarEmailDeSolicitacaoDeNotaFiscal() {
        return parametroRepository
                .findById(Parametros.DEVE_ENVIAR_EMAIL_NOTA_FISCAL.getId())
                .map(parametro -> "S".equals(parametro.getValor()))
                .orElse(false);
    }

}
