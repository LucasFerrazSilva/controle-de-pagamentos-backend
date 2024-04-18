package com.ferraz.controledepagamentosbackend.domain.parameters.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateParametroDTO(
        @NotBlank
        String nome,
        @NotBlank
        String valor
) {
}
