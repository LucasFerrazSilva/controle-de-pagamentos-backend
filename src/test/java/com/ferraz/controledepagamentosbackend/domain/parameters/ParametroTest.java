package com.ferraz.controledepagamentosbackend.domain.parameters;

import static com.ferraz.controledepagamentosbackend.domain.user.UserStatus.ATIVO;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.utils.TesteUtils;

class ParametroTest {

    private Parametro parametro;
    private User user;

    @BeforeEach
    void setUp() {
        Long id = 1L;
        String email = "teste@teste.com";
        String password = TesteUtils.DEFAULT_PASSWORD;
        String name = "Nome Teste";
        this.user = new User(id, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), UsuarioPerfil.ROLE_ADMIN, ATIVO, LocalDateTime.now(), null, null, null);

        parametro = new Parametro(1L, "novo_parametro", "novo_valor", ParametroStatus.ATIVO, LocalDateTime.now(), user, LocalDateTime.now(), user);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getStatus() {
        assertThat(parametro.getStatus()).isEqualTo(ParametroStatus.ATIVO);
    }

    @Test
    void getCreateDatetime() {
        assertThat(parametro.getCreateDatetime()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void getCreateUser() {
        assertThat(parametro.getCreateUser()).isEqualTo(user);
    }

    @Test
    void getUpdateDatetime() {
        assertThat(parametro.getUpdateDatetime()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void getUpdateUser() {
        assertThat(parametro.getUpdateUser()).isEqualTo(user);
    }

    @Test
    void testEquals() {
        Parametro parametro2 = new Parametro(1L, "novo_parametro", "novo_valor", ParametroStatus.ATIVO, LocalDateTime.now(), user, LocalDateTime.now(), user);
        assertThat(parametro.equals(parametro2)).isTrue();

    }

    @Test
    void testIsNotEqualsTo(){
        Parametro parametro2 = new Parametro(2L, "novo_parametro", "novo_valor", ParametroStatus.ATIVO, LocalDateTime.now(), user, LocalDateTime.now(), user);
        assertThat(parametro.equals(parametro2)).isFalse();
    }
}