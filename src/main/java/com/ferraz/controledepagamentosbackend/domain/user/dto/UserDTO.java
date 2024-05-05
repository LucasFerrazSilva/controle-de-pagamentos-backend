package com.ferraz.controledepagamentosbackend.domain.user.dto;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;

import java.math.BigDecimal;

public record UserDTO(
        Long id,

        String nome,

        String email,

        String perfil,

        UserStatus status,

        BigDecimal salario
) {
    public UserDTO(User user) {
        this(user.getId(), user.getNome(), user.getEmail(), user.getPerfil(), user.getStatus(), user.getSalario());
    }

}
