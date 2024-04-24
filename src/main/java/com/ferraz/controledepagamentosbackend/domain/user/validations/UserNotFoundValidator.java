package com.ferraz.controledepagamentosbackend.domain.user.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.exceptions.UserNotFoundException;

@Component
public class UserNotFoundValidator implements DeleteUserValidator{

	private UserRepository repository;
	
	public UserNotFoundValidator(UserRepository repository) {
		this.repository = repository;
	}
	@Override
	public void validate(Long id) {
		if(!repository.existsById(id)) {
			throw new UserNotFoundException("Usario com o id " + id + " não encontrado");
		}
	}
	

}
