package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoRepository;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoService;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoStatus;
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
import org.springframework.test.context.ActiveProfiles;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.createRandomUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificacaoControllerTest {

    @Autowired
    private NotificacaoService notificacaoService;
    @Autowired
    private NotificacaoRepository notificacaoRepository;
    @Autowired
    private UserRepository userRepository;

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
    void testListarPorUsuario() {
        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Deve marcar notificacao como visualizada quando passar dados validos")
    void testMarcarNotificacaoComoVisualizada() {
        // Given

        // When

        // Then
    }

}