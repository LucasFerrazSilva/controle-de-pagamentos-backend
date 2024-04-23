package com.ferraz.controledepagamentosbackend.domain.user.DTO;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;

public record DadosListagemUserDTO(Long id, String nome, String email, 
			String perfil, UserStatus status) {
	
	public DadosListagemUserDTO(User user) {
		this(user.getId(), user.getNome() ,user.getEmail(),user.getPerfil(), user.getStatus());
	}


}


