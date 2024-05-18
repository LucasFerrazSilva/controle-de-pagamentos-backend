package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.RotinaDeFechamentoService;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalRepository;
import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoRepository;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoStatus;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RotinaDeFechamentoControllerTest {

    @Autowired
    private MockMvc mvc;
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
    @DisplayName("Deve realizar a rotina de fechamento corretamente")
    void getAllParameters() throws Exception {
        // Given
        User randomUser1 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User randomUser2 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User aprovador = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
        HttpHeaders token = login(mvc, aprovador);

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
        mvc.perform(MockMvcRequestBuilders.post("/rotina-de-fechamento").headers(token)).andReturn().getResponse();

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