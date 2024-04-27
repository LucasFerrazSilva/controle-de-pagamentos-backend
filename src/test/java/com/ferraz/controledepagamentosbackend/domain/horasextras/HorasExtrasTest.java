package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HorasExtrasTest {

    @Test
    void testConstructor() {
        // Given
        Long id = 1l;
        User user = new User();
        user.setId(1l);
        LocalDateTime dataHoraInicio = LocalDateTime.now().minusHours(3);
        LocalDateTime dataHoraFim = LocalDateTime.now().minusHours(1);
        String descricao = "desc";
        User aprovador = new User();
        aprovador.setId(2l);
        HorasExtrasStatus status = HorasExtrasStatus.SOLICITADO;
        LocalDateTime createDatetime = LocalDateTime.now();
        User createUser = user;
        LocalDateTime updateDatetime = LocalDateTime.now();
        User updateUser = user;

        // When
        HorasExtras horasExtras =
                new HorasExtras(id, user, dataHoraInicio, dataHoraFim, descricao, aprovador, status,
                        createDatetime, createUser, updateDatetime, updateUser);
        HorasExtras horasExtras2 =
                new HorasExtras(id, user, dataHoraInicio, dataHoraFim, descricao, aprovador, status,
                        null, null, null, null);
        horasExtras2.setCreateDatetime(createDatetime);
        horasExtras2.setCreateUser(createUser);
        horasExtras2.setUpdateDatetime(updateDatetime);
        horasExtras2.setUpdateUser(updateUser);

        // Then
        assertThat(horasExtras).isEqualTo(horasExtras2);
        assertThat(horasExtras.getCreateDatetime()).isEqualTo(createDatetime);
        assertThat(horasExtras.getCreateUser()).isEqualTo(createUser);
        assertThat(horasExtras.getUpdateDatetime()).isEqualTo(updateDatetime);
        assertThat(horasExtras.getUpdateUser()).isEqualTo(updateUser);
    }

}