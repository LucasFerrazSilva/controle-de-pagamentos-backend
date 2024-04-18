package com.ferraz.controledepagamentosbackend.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record DadosUserDTO(
		
		@NotBlank
		String nome,
		
		@NotBlank
		@NotNull
		@Email
		String email,
		
		@NotNull
		@NotBlank
		String senha,
		
		@NotNull
		BigDecimal salario,
		
		@NotBlank
		@NotNull
		String perfil

		) {

}
