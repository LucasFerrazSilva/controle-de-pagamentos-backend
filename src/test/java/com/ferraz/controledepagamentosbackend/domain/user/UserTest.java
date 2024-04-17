package com.ferraz.controledepagamentosbackend.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    public void testGetters() {
        // Given
        Long id = 1l;
        String nome = "Usuario";
        String email = "usuario@mail.com";
        String senha = "1234";
        BigDecimal salario = new BigDecimal("1234");
        String perfil = "ROLE_ADMIN";
        UserStatus status = UserStatus.ATIVO;
        LocalDateTime createDatetime = LocalDateTime.now();
        User createUser = new User();
        createUser.setId(2l);
        LocalDateTime updateDatetime = LocalDateTime.now().plusHours(1);
        User updateUser = new User();
        updateUser.setId(3l);

        // When
        User user = new User(id, nome, email, senha, salario, perfil, status, createDatetime, createUser, updateDatetime, updateUser);

        // Then
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getNome()).isEqualTo(nome);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getUsername()).isEqualTo(email);
        assertThat(user.getSenha()).isEqualTo(senha);
        assertThat(user.getSalario()).isEqualTo(salario);
        assertThat(user.getPerfil()).isEqualTo(perfil);
        assertThat(user.getStatus()).isEqualTo(status);
        assertThat(user.getCreateDatetime()).isEqualTo(createDatetime);
        assertThat(user.getCreateUser().getId()).isEqualTo(createUser.getId());
        assertThat(user.getUpdateDatetime()).isEqualTo(updateDatetime);
        assertThat(user.getUpdateUser().getId()).isEqualTo(updateUser.getId());
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isEqualTo(status == UserStatus.ATIVO);

        assertThat(user.getAuthorities()).isEqualTo(List.of(new SimpleGrantedAuthority(perfil)));
    }

}