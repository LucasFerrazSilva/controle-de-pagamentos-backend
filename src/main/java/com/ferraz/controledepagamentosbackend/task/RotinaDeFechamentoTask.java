package com.ferraz.controledepagamentosbackend.task;

import com.ferraz.controledepagamentosbackend.domain.horasextras.RotinaDeFechamentoService;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametros;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RotinaDeFechamentoTask {

    private final ParametroRepository parametroRepository;
    private final RotinaDeFechamentoService rotinaDeFechamentoService;

    public RotinaDeFechamentoTask(ParametroRepository parametroRepository, RotinaDeFechamentoService rotinaDeFechamentoService) {
        this.parametroRepository = parametroRepository;
        this.rotinaDeFechamentoService = rotinaDeFechamentoService;
    }

    @Scheduled(fixedDelay = 1000)
//    @Scheduled(cron = "0 0 * * * *")
    public void executarRotinaDeFechamento() {
        String diaHoraFechamento = parametroRepository.findById(Parametros.DIA_HORA_FECHAMENTO.getId()).orElseThrow().getValor();
        Integer diaFechamento = Integer.parseInt(diaHoraFechamento.split(",")[0]);
        Integer horaFechamento = Integer.parseInt(diaHoraFechamento.split(",")[1]);

        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfMonth() == diaFechamento && now.getHour() == horaFechamento) {
            rotinaDeFechamentoService.executarFechamento();
        }
    }

}
