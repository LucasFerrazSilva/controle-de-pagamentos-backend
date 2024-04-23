package com.ferraz.controledepagamentosbackend.domain.user.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosUserDTO(
		@NotBlank
		String nome,
		
		@NotBlank
		@Email
		String email,
		
		@NotBlank
		String senha,
		
		@NotNull
		BigDecimal salario,
		
		@NotBlank
		String perfil
		
		) {

}
