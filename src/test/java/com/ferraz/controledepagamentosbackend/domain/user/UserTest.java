package com.ferraz.controledepagamentosbackend.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class UserTest {



    @Test
    @DisplayName("Deve retornar valores informados quando busca usando os getters")
    void testGetters() {
        // Given / When
        Long id = 1l;
        String nome = "Usuario";
        String email = "usuario@mail.com";
        String senha = "1234";
        BigDecimal salario = new BigDecimal("1234");
        UsuarioPerfil perfil = UsuarioPerfil.ROLE_ADMIN;
        UserStatus status = UserStatus.ATIVO;
        LocalDateTime createDatetime = LocalDateTime.now();
        User createUser = new User();
        createUser.setId(2l);
        LocalDateTime updateDatetime = LocalDateTime.now().plusHours(1);
        User updateUser = new User();
        updateUser.setId(3l);
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
        assertThat(user.getCreateDateTime()).isEqualTo(createDatetime);
        assertThat(user.getCreateUser().getId()).isEqualTo(createUser.getId());
        assertThat(user.getUpdateDatetime()).isEqualTo(updateDatetime);
        assertThat(user.getUpdateUser().getId()).isEqualTo(updateUser.getId());
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isEqualTo(status == UserStatus.ATIVO);

        assertThat(user.getAuthorities()).isEqualTo(List.of(new SimpleGrantedAuthority(perfil.toString())));
    }

    @Test
    @DisplayName("Deve informar que usuario nao esta habilitado quando status for INATIVO")
    void testIsEnabled_False() {
        // Given
        User user = createUser();
        user.setStatus(UserStatus.INATIVO);

        // When
        boolean enabled = user.isEnabled();

        // Then
        assertThat(enabled).isFalse();
    }

    @Test
    @DisplayName("Equals e Hashcode devem considerar somente campos id, email e nome")
    void testIsEqualsAndHashcode() {
        // Given
        User user = createUser();
        User user2 = createUser();
        user2.setPerfil(UsuarioPerfil.ROLE_GESTOR); // troca campo irrelevante
        User user3 = createUser();
        user3.setId(99l); // troca campo relevante

        // When
        boolean equals1 = user.equals(user2);
        boolean equals2 = user.equals(user3);

        // Then
        assertThat(equals1).isTrue();
        assertThat(equals2).isFalse();
    }

    @Test
    @DisplayName("deve testar se usuário desativou")
    void testDeactivateUser(){
        // Given
        User user = createUser();
        User user2 = createUser();
        user2.setNome("teste_user");
        user2.setId(99L);

        // when
        user.deactivate(user2);

        // Then
        assertThat(user.getStatus()).isEqualTo(UserStatus.INATIVO);
        assertThat(user.getUpdateUser()).isEqualTo(user2);
    }

    private static User createUser() {
        Long id = 1l;
        String nome = "Usuario";
        String email = "usuario@mail.com";
        String senha = "1234";
        BigDecimal salario = new BigDecimal("1234");
        UsuarioPerfil perfil = UsuarioPerfil.ROLE_ADMIN;
        UserStatus status = UserStatus.ATIVO;
        LocalDateTime createDatetime = LocalDateTime.now();
        User createUser = new User();
        createUser.setId(2l);
        LocalDateTime updateDatetime = LocalDateTime.now().plusHours(1);
        User updateUser = new User();
        updateUser.setId(3l);
        return new User(id, nome, email, senha, salario, perfil, status, createDatetime, createUser, updateDatetime, updateUser);
    }

}