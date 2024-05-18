package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalRepository;
import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoRepository;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoStatus;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametros;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.createRandomHorasExtras;
import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.createRandomUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RotinaDeFechamentoServiceTest {

    @Autowired
    private RotinaDeFechamentoService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HorasExtrasRepository horasExtrasRepository;
    @Autowired
    private NotaFiscalRepository notaFiscalRepository;
    @Autowired
    private NotificacaoRepository notificacaoRepository;
    @Autowired
    private ParametroRepository parametroRepository;

    @Test
    @DisplayName("Deve calcular o valor da nota corretamente")
    void testCalcularValorNota() {
        // Given
        int horasMes = Integer.parseInt(parametroRepository.findById(Parametros.HORAS_MES.getId()).orElseThrow().getValor());

        BigDecimal valorEsperado = BigDecimal.ZERO;

        User randomUser = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        valorEsperado = valorEsperado.add(randomUser.getSalario());

        User aprovador = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);

        List<HorasExtras> horasExtrasList = List.of(
                createRandomHorasExtras(randomUser, aprovador, horasExtrasRepository),
                createRandomHorasExtras(randomUser, aprovador, horasExtrasRepository)
        );

        BigDecimal valorHora = randomUser.getSalario().divide(new BigDecimal(horasMes), 2, RoundingMode.HALF_UP);

        for (HorasExtras horasExtras: horasExtrasList) {
            long horas = service.calcularHoras(horasExtras);
            BigDecimal valorHorasExtras = valorHora.multiply(new BigDecimal(horas));
            valorEsperado = valorEsperado.add(valorHorasExtras);
        }

        // When
        BigDecimal total = service.calcularValorDaNota(randomUser, horasExtrasList);

        // Then
        //  Total deve ser o esperado
        assertThat(total).isEqualByComparingTo(valorEsperado);
    }

    @Test
    @DisplayName("Deve calcular o total de horas corretamente")
    void testCalcularHoras() {
        // Given
        User randomUser = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User aprovador = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
        HorasExtras horasExtras = createRandomHorasExtras(randomUser, aprovador, horasExtrasRepository);
        int horas = 2;
        horasExtras.setDataHoraInicio(LocalDateTime.now());
        horasExtras.setDataHoraFim(LocalDateTime.now().plusHours(horas));

        // When
        long horasCalculadas = service.calcularHoras(horasExtras);

        // Then
        assertThat(horasCalculadas).isEqualTo(horas);
    }


    @Test
    @DisplayName("Deve executar a rotina de fechamento corretamente")
    void testExecutarFechamento() throws InterruptedException {
        // Given
        User randomUser1 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User randomUser2 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User aprovador = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);

        HorasExtras horasExtrasUser1 = createRandomHorasExtras(randomUser1, aprovador, horasExtrasRepository);
        horasExtrasUser1.setStatus(HorasExtrasStatus.APROVADO);
        horasExtrasRepository.save(horasExtrasUser1);
        HorasExtras horasExtras2User1 = createRandomHorasExtras(randomUser1, aprovador, horasExtrasRepository);
        horasExtras2User1.setStatus(HorasExtrasStatus.APROVADO);
        horasExtrasRepository.save(horasExtras2User1);
        HorasExtras horasExtrasUser2 = createRandomHorasExtras(randomUser2, aprovador, horasExtrasRepository);
        horasExtrasUser2.setStatus(HorasExtrasStatus.APROVADO);
        horasExtrasRepository.save(horasExtrasUser2);

        BigDecimal valorEsperado1 = service.calcularValorDaNota(randomUser1, List.of(horasExtrasUser1, horasExtras2User1));
        BigDecimal valorEsperado2 = service.calcularValorDaNota(randomUser2, List.of(horasExtrasUser2));

        // When
        service.executarFechamento();

        // Then
        //  Foi criado um registro de nota fiscal para cada um com os valores corretos e status SOLICITADA
        List<NotaFiscal> notasFiscaisUser1 = notaFiscalRepository.findByUser(randomUser1);
        assertThat(notasFiscaisUser1).hasSize(1);
        assertThat(notasFiscaisUser1.get(0).getValor()).isEqualByComparingTo(valorEsperado1);

        List<NotaFiscal> notasFiscaisUser2 = notaFiscalRepository.findByUser(randomUser2);
        assertThat(notasFiscaisUser1).hasSize(1);
        assertThat(notasFiscaisUser2.get(0).getValor()).isEqualByComparingTo(valorEsperado2);

        //  Foram criadas notificacoes para cada um
        List<Notificacao> notificacoesUser1 = notificacaoRepository.findTop5ByUserAndStatusNotOrderByCreateDatetimeDesc(randomUser1, NotificacaoStatus.INATIVA);
        assertThat(notificacoesUser1).hasSize(1);

        List<Notificacao> notificacoesUser2 = notificacaoRepository.findTop5ByUserAndStatusNotOrderByCreateDatetimeDesc(randomUser2, NotificacaoStatus.INATIVA);
        assertThat(notificacoesUser2).hasSize(1);
    }

}