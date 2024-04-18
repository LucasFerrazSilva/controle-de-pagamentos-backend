package com.ferraz.controledepagamentosbackend.domain.user;

import java.math.BigDecimal;

public record DadosListagemUserDTO(Long id, String nome, String senha, BigDecimal salario,
			String perfil, String status) {
	
	public DadosListagemUserDTO(User user) {
		this(user.getId(), user.getNome(), user.getSenha(), user.getSalario(), user.getPerfil(), user.getStatus());
	}

	
}


