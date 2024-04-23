package com.ferraz.controledepagamentosbackend.domain.user.DTO;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;

public record UserDTO(
		Long id,
		
		String nome,
		
		String email,
		
		String perfil,
		
		UserStatus status
		) {
	
	public UserDTO(User user) {
		this(user.getId(), user.getNome(), user.getEmail(), user.getPerfil(), user.getStatus());
	}

}
