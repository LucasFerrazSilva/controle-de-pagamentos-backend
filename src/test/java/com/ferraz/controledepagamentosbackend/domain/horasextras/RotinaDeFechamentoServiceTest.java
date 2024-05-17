package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalRepository;
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
import java.time.temporal.ChronoUnit;
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
//    @Autowired
//    private

    @Test
    @DisplayName("Deve executar a rotina de fechamento corretamente")
    void testExecutarFechamento() {
        // Given
        User randomUser1 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User randomUser2 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User aprovador = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);

        HorasExtras horasExtrasUser1 = createRandomHorasExtras(randomUser1, aprovador, horasExtrasRepository);
        HorasExtras horasExtras2User1 = createRandomHorasExtras(randomUser1, aprovador, horasExtrasRepository);
        HorasExtras horasExtrasUser2 = createRandomHorasExtras(randomUser2, aprovador, horasExtrasRepository);

        BigDecimal valorEsperado1 = calcularValorEsperado(randomUser1, List.of(horasExtrasUser1, horasExtras2User1));
        BigDecimal valorEsperado2 = calcularValorEsperado(randomUser2, List.of(horasExtrasUser2));

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

    }

    private BigDecimal calcularValorEsperado(User user, List<HorasExtras> horasExtras) {
        BigDecimal valorBase = user.getSalario();
        long totalHorasExtras = horasExtras.stream().map(this::calcularHoras).reduce(0l, Long::sum);
        BigDecimal valorHorasExtras = valorBase.divide(new BigDecimal(200)).multiply(new BigDecimal(totalHorasExtras));
        return valorBase.add(valorHorasExtras);
    }

    private long calcularHoras(HorasExtras horasExtras) {
        return ChronoUnit.HOURS.between(horasExtras.getDataHoraInicio(), horasExtras.getDataHoraFim());
    }

}