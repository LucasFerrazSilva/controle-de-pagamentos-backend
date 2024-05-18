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

    @Scheduled(cron = "0 0 * * * *")
    public void executarRotinaDeFechamento() throws InterruptedException {
        String diaHoraFechamento = parametroRepository.findById(Parametros.DIA_HORA_FECHAMENTO.getId()).orElseThrow().getValor();
        int diaFechamento = Integer.parseInt(diaHoraFechamento.split(",")[0]);
        int horaFechamento = Integer.parseInt(diaHoraFechamento.split(",")[1]);

        int diaAtual = LocalDateTime.now().getDayOfMonth();
        int horaAtual = LocalDateTime.now().getHour();
        if (diaAtual == diaFechamento && horaAtual == horaFechamento) {
            rotinaDeFechamentoService.executarFechamento();
        }
    }

}
