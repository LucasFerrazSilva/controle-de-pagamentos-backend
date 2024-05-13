package com.ferraz.controledepagamentosbackend.domain.link;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LinkTest {

    @Test
    void testConstructor() {
        // Given
        UUID id = UUID.randomUUID();
        HorasExtras horasExtras = new HorasExtras();
        AcaoLink acao = AcaoLink.APROVAR;
        LinkStatus status = LinkStatus.CRIADO;
        LocalDateTime createDatetime = LocalDateTime.now().minusHours(1);
        User createUser = new User();
        LocalDateTime updateDatetime = LocalDateTime.now();
        User updateUser = new User();

        // When
        Link link1 = new Link(id, horasExtras, acao, status, createDatetime, createUser, updateDatetime, updateUser);
        Link link2 = new Link(id, horasExtras, acao, status, createDatetime, createUser, updateDatetime, updateUser);
        link1.setCreateUser(createUser);
        link1.setCreateDatetime(createDatetime);
        link1.setUpdateUser(updateUser);
        link1.setUpdateDatetime(updateDatetime);

        // Then
        assertThat(link1).isEqualTo(link2);
        assertThat(link1.getCreateUser()).isEqualTo(createUser);
        assertThat(link1.getCreateDatetime()).isEqualTo(createDatetime);
        assertThat(link1.getUpdateUser()).isEqualTo(updateUser);
        assertThat(link1.getUpdateDatetime()).isEqualTo(updateDatetime);
    }

    @Test
    void testAtualizar() {
        // Given
        UUID id = UUID.randomUUID();
        HorasExtras horasExtras = new HorasExtras();
        AcaoLink acao = AcaoLink.APROVAR;
        LinkStatus status = LinkStatus.CRIADO;
        LocalDateTime createDatetime = LocalDateTime.now().minusHours(1);
        User createUser = new User();
        LocalDateTime updateDatetime = LocalDateTime.now();
        User updateUser = new User();
        Link link1 = new Link(id, horasExtras, acao, status, createDatetime, createUser, updateDatetime, updateUser);

        LinkStatus novoStatus = LinkStatus.EXPIRADO;
        User novoUsuario = new User();

        // When
        link1.atualizar(novoStatus, novoUsuario);

        // Then
        assertThat(link1.getStatus()).isEqualTo(novoStatus);
        assertThat(link1.getUpdateUser()).isEqualTo(novoUsuario);
        assertThat(link1.getUpdateDatetime()).isNotNull();
    }

}