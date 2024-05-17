package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoRepository;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoService;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoStatus;
import com.ferraz.controledepagamentosbackend.domain.notificacao.dto.NotificacaoDTO;
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
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificacaoControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private NotificacaoService notificacaoService;
    @Autowired
    private NotificacaoRepository notificacaoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JacksonTester<List<NotificacaoDTO>> notificaoListJackson;

    private static final String ENDPOINT = "/notificacoes";

    @Test
    @DisplayName("Deve criar uma notificação quando passar dados válidos")
    void testCriar() {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        String descricao = "Aprovador X aprovou a sua hora extra do dia 20/05/2024";
        String path = "/horas-extras";

        // When
        Notificacao notificacao = notificacaoService.create(user, descricao, path);

        // Then
        assertThat(notificacao).isNotNull();
        assertThat(notificacao.getId()).isNotNull();
        assertThat(notificacao.getUser()).isEqualTo(user);
        assertThat(notificacao.getDescricao()).isEqualTo(descricao);
        assertThat(notificacao.getPath()).isEqualTo(path);
        assertThat(notificacao.getStatus()).isEqualTo(NotificacaoStatus.CRIADA);
        assertThat(notificacao.getCreateDatetime()).isNotNull();
        assertThat(notificacaoRepository.findById(notificacao.getId())).isPresent();
    }

    @Test
    @DisplayName("Deve retornar lista de notificacoes quando usuario informado possuir notificacoes")
    void testListarPorUsuario() throws Exception {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        HttpHeaders token = login(mvc, user);
        Notificacao notificacao1 = criarNotificacao(user, notificacaoService);
        Notificacao notificacao2 = criarNotificacao(user, notificacaoService);
        RequestBuilder request = get(ENDPOINT).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<NotificacaoDTO> list = notificaoListJackson.parse(response.getContentAsString()).getObject();
        assertThat(list)
                .hasSize(2)
                .contains(new NotificacaoDTO(notificacao1))
                .contains(new NotificacaoDTO(notificacao2));
    }

    @Test
    @DisplayName("Deve marcar notificacao como visualizada quando passar dados validos")
    void testMarcarNotificacaoComoVisualizada() throws Exception {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        HttpHeaders token = login(mvc, user);
        criarNotificacao(user, notificacaoService);
        criarNotificacao(user, notificacaoService);
        RequestBuilder request = post(ENDPOINT + "/marcar-como-visualizadas").headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        List<Notificacao> notificacoes = notificacaoRepository.findTop5ByUserAndStatusNotOrderByCreateDatetimeDesc(user, NotificacaoStatus.INATIVA);
        assertThat(notificacoes).hasSize(2);
        notificacoes.forEach(notificacao -> assertThat(notificacao.getStatus()).isEqualTo(NotificacaoStatus.VISUALIZADA));
    }

}