package com.ferraz.controledepagamentosbackend.domain.user.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;

public record DadosAtualizacaoUserDTO(

		String nome,

		@Email String email,

		String senha,

		BigDecimal salario,

		String perfil) {

}
