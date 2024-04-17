package com.ferraz.controledepagamentosbackend.infra.security.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O campo e-mail não pode estar nulo ou vazio.")
        String email,
        @NotBlank(message = "O campo senha não pode estar nulo ou vazio.")
        String password
) {}
