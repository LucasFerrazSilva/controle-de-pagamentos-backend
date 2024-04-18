package com.ferraz.controledepagamentosbackend.domain.user;

import java.math.BigDecimal;

public record DadosListagemUserDTO(Long id, String nome, String senha, BigDecimal salario,
			String perfil, UserStatus status) {
	
	public DadosListagemUserDTO(User user) {
		this(user.getId(), user.getNome(), user.getSenha(), user.getSalario(), user.getPerfil(), user.getStatus());
	}
}


