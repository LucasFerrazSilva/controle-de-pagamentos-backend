package com.ferraz.controledepagamentosbackend.domain.user.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.DTO.DadosUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.exceptions.EmailAlreadyInUseException;

@Component
public class CreateUserEmailValidator implements CreateUserValidator{

	private UserRepository repository;
	
	public CreateUserEmailValidator(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void validator(DadosUserDTO dados) {
		if(repository.existsByEmail(dados.email())) {
			throw new EmailAlreadyInUseException("Email ja cadastrado!");
		}
		
	}

}
