package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotaFiscalTest {

    @Test
    @DisplayName("Deve testar os métodos da entidade")
    void testNotaFiscal() {
        // Given
        Long id = 1L;
        User user = new User();
        user.setId(1L);
        Integer mes = 1;
        Integer ano = 2024;
        BigDecimal valor = BigDecimal.valueOf(2000);
        NotaFiscalStatus status = NotaFiscalStatus.SOLICITADA;
        String filePath = "teste";
        LocalDateTime createDatetime = LocalDateTime.now();
        User createUser = user;
        LocalDateTime updateDatetime = LocalDateTime.now();
        User updateUser = user;


        // When
        NotaFiscal notaFiscal = new NotaFiscal(id, user, mes, ano, valor, status, filePath,
                createDatetime, createUser, updateDatetime, updateUser);
        NotaFiscal notaFiscal2 = new NotaFiscal(id, user, mes, ano, valor, status, filePath,
                null, null, null, null);

        notaFiscal2.setCreateDatetime(createDatetime);
        notaFiscal2.setCreateUser(createUser);
        notaFiscal2.setUpdateDatetime(updateDatetime);
        notaFiscal2.setUpdateUser(updateUser);

        // Then
        assertThat(notaFiscal).isEqualTo(notaFiscal2);
        assertThat(notaFiscal.getCreateDatetime()).isEqualTo(createDatetime);
        assertThat(notaFiscal.getCreateUser()).isEqualTo(createUser);
        assertThat(notaFiscal.getUpdateDatetime()).isEqualTo(updateDatetime);
        assertThat(notaFiscal.getUpdateUser()).isEqualTo(updateUser);

    }
}