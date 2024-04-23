package com.ferraz.controledepagamentosbackend.domain.user.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.DTO.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.exceptions.EmailAlreadyInUseException;

@Component
public class UptadeUserEmailValidator implements UpdateUserValidator{
	
	private UserRepository repository;
	
	public UptadeUserEmailValidator(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void validate(DadosAtualizacaoUserDTO dto, Long id) {
		if(repository.existsByEmailAndIdNot(dto.email(), id)) {
			throw new EmailAlreadyInUseException("Email ja Cadastrado!");
		}
	}


}
