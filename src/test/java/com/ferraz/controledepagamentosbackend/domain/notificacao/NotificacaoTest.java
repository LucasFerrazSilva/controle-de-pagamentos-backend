package com.ferraz.controledepagamentosbackend.domain.notificacao;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificacaoTest {

    @Test
    void test() {
        // Given
        Long id = 1L;
        User user = new User();
        user.setId(1l);
        String descricao = "Notificacao x";
        String path = "/path";
        NotificacaoStatus status = NotificacaoStatus.CRIADA;
        LocalDateTime createDatetime = LocalDateTime.now();
        User createUser = new User();
        user.setId(2l);
        LocalDateTime updateDatetime = LocalDateTime.now();
        User updateUser = new User();
        user.setId(3l);

        // When
        Notificacao notificacao = new Notificacao(id, user, descricao, path, status, createDatetime, createUser, updateDatetime, updateUser);
        Notificacao notificacao2 = new Notificacao(id, user, descricao, path, status, createDatetime, createUser, updateDatetime, updateUser);

        // Then
        assertThat(notificacao).isEqualTo(notificacao2);
        assertThat(notificacao.getCreateUser()).isEqualTo(createUser);
        assertThat(notificacao.getCreateDatetime()).isEqualTo(createDatetime);
        assertThat(notificacao.getUpdateUser()).isEqualTo(updateUser);
        assertThat(notificacao.getUpdateDatetime()).isEqualTo(updateDatetime);
    }

}