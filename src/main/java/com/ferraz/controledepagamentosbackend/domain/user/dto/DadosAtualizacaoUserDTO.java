package com.ferraz.controledepagamentosbackend.domain.user.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;

public record DadosAtualizacaoUserDTO(

		String nome,

		@Email String email,

		BigDecimal salario,

		String perfil) {

}
