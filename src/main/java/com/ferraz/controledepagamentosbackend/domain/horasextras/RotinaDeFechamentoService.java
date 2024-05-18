package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.emails.NotificarSolicitacaoDeNotaFiscalService;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalService;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametros;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserService;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RotinaDeFechamentoService {

    private final ParametroRepository parametroRepository;
    private final UserService userService;
    private final HorasExtrasRepository horasExtrasRepository;
    private final NotaFiscalService notaFiscalService;
    private final NotificarSolicitacaoDeNotaFiscalService notificarSolicitacaoDeNotaFiscalService;

    public RotinaDeFechamentoService(ParametroRepository parametroRepository, UserService userService,
                                     HorasExtrasRepository horasExtrasRepository, NotaFiscalService notaFiscalService,
                                     NotificarSolicitacaoDeNotaFiscalService notificarSolicitacaoDeNotaFiscalService) {
        this.parametroRepository = parametroRepository;
        this.userService = userService;
        this.horasExtrasRepository = horasExtrasRepository;
        this.notaFiscalService = notaFiscalService;
        this.notificarSolicitacaoDeNotaFiscalService = notificarSolicitacaoDeNotaFiscalService;
    }

    @Transactional
    public void executarFechamento() {
        List<User> prestadores = userService.listarPorPerfil(UsuarioPerfil.ROLE_USER);
        List<NotaFiscal> notasFiscais = new ArrayList<>();

        prestadores.forEach(prestador -> {
            // filtrar somente horas extras do ultimo dia de fechamento + 1 ate data atual
            List<HorasExtras> horasExtras =
                    horasExtrasRepository.findByUserAndStatusAndPagoIsFalse(prestador, HorasExtrasStatus.APROVADO);

            BigDecimal valorDaNota = calcularValorDaNota(prestador, horasExtras);

            horasExtras.forEach(HorasExtras::marcarComoPaga);
            horasExtrasRepository.saveAll(horasExtras);

            NotaFiscal notaFiscal = notaFiscalService.create(new NovaNotaFiscalDTO(prestador, valorDaNota));
            notasFiscais.add(notaFiscal);
        });

        notasFiscais.forEach(notificarSolicitacaoDeNotaFiscalService::enviar);
    }

    public BigDecimal calcularValorDaNota(User user, List<HorasExtras> horasExtras) {
        int horasMes = Integer.parseInt(parametroRepository.findById(Parametros.HORAS_MES.getId()).orElseThrow().getValor());
        BigDecimal valorBase = user.getSalario();
        long totalHorasExtras = horasExtras.stream().map(this::calcularHoras).reduce(0l, Long::sum);
        BigDecimal valorHorasExtras =
                valorBase
                        .divide(new BigDecimal(horasMes), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(totalHorasExtras));
        return valorBase.add(valorHorasExtras);
    }

    public long calcularHoras(HorasExtras horasExtras) {
        return ChronoUnit.HOURS.between(horasExtras.getDataHoraInicio(), horasExtras.getDataHoraFim());
    }

}
