package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.validations.AtualizarHorasExtrasValidator;
import com.ferraz.controledepagamentosbackend.domain.horasextras.validations.NovasHorasExtrasValidator;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.infra.email.EmailDTO;
import com.ferraz.controledepagamentosbackend.infra.email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Service
public class HorasExtrasService {

    private final HorasExtrasRepository repository;
    private final UserRepository userRepository;
    private final List<NovasHorasExtrasValidator> novasHorasExtrasValidators;
    private final List<AtualizarHorasExtrasValidator> atualizarHorasExtrasValidators;
    private SpringTemplateEngine templateEngine;
    private EmailService emailService;
    @Value("${application_sender}")
    private String applicationSender;

    public HorasExtrasService(HorasExtrasRepository repository, UserRepository userRepository, List<NovasHorasExtrasValidator> novasHorasExtrasValidators, List<AtualizarHorasExtrasValidator> atualizarHorasExtrasValidators, SpringTemplateEngine templateEngine, EmailService emailService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.novasHorasExtrasValidators = novasHorasExtrasValidators;
        this.atualizarHorasExtrasValidators = atualizarHorasExtrasValidators;
        this.templateEngine = templateEngine;
        this.emailService = emailService;
    }

    @Transactional
    public HorasExtras create(NovasHorasExtrasDTO dto) {
        novasHorasExtrasValidators.forEach(validator -> validator.validate(dto));
        User aprovador = userRepository.findById(dto.idAprovador()).orElseThrow();
        HorasExtras horasExtras = new HorasExtras(dto, getLoggedUser(), aprovador);
        repository.save(horasExtras);
        enviarEmailDeSolicitacaoDeAprovacao(horasExtras);
        return horasExtras;
    }

    private void enviarEmailDeSolicitacaoDeAprovacao(HorasExtras horasExtras) {
        String subject = "Solicitação de aprovação de horas extras";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Map<String, Object> variables = new HashMap<>();
        variables.put("nomeAprovador", horasExtras.getAprovador().getNome());
        variables.put("nomeSolicitante", horasExtras.getUser().getNome());
        variables.put("inicioHorasExtras", horasExtras.getDataHoraInicio().format(formatter));
        variables.put("fimHorasExtras", horasExtras.getDataHoraFim().format(formatter));
        variables.put("descricao", horasExtras.getDescricao());
        Context context = new Context();
        context.setVariables(variables);
        String html = templateEngine.process("solicitacao-de-analise.html", context);

        EmailDTO emailDTO = new EmailDTO(applicationSender, horasExtras.getAprovador().getEmail(), subject, html);

        this.emailService.sendMailSMTP(emailDTO);
    }

    public Page<HorasExtras> list(Pageable pageable, Long idUsuario, Long idAprovador, LocalDate dataInicio,
                                  LocalDate dataFim, String descricao, HorasExtrasStatus status) {

        if (getLoggedUser().getPerfil().equals(UsuarioPerfil.ROLE_USER))
            idUsuario = getLoggedUser().getId();

        Page<HorasExtras> page = repository.findByFiltros(pageable, idUsuario, idAprovador, descricao, status);

        if (dataInicio != null) {
            LocalDateTime dataHoraInicio = dataInicio.atTime(LocalTime.MIN);
            List<HorasExtras> list = page.stream().filter(horasExtras -> dataHoraInicio.isBefore(horasExtras.getDataHoraInicio())).toList();
            page = new PageImpl<>(list, pageable, list.size());
        }

        if (dataFim != null) {
            LocalDateTime dataHoraFim = dataFim.atTime(LocalTime.MAX);
            List<HorasExtras> list = page.stream().filter(horasExtras -> dataHoraFim.isAfter(horasExtras.getDataHoraFim())).toList();
            page = new PageImpl<>(list, pageable, list.size());
        }

        return page;
    }

    public HorasExtras findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public HorasExtras update(Long id, AtualizarHorasExtrasDTO atualizarHorasExtrasDTO) {
        atualizarHorasExtrasValidators.forEach(validator -> validator.validate(id, atualizarHorasExtrasDTO));
        HorasExtras horasExtras = repository.findById(id).orElseThrow();
        User aprovador = userRepository.findById(atualizarHorasExtrasDTO.idAprovador()).orElseThrow();
        horasExtras.update(atualizarHorasExtrasDTO, getLoggedUser(), aprovador);
        repository.save(horasExtras);
        enviarEmailDeSolicitacaoDeAprovacao(horasExtras);
        return horasExtras;
    }

    @Transactional
    public void delete(Long id) {
        HorasExtras horasExtras = repository.findById(id).orElseThrow();
        horasExtras.inativar(getLoggedUser());
        repository.save(horasExtras);
    }

}
