package com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record NovaNotaFiscalDTO(
        @NotNull
        Long idUser,
        @NotNull
        Integer mes,
        @NotNull
        Integer ano,
        @NotNull
        @Min(0)
        BigDecimal valor
) {
        public NovaNotaFiscalDTO(User prestador, BigDecimal valor) {
                this(
                        prestador.getId(),
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear(),
                        valor
                );
        }
}
