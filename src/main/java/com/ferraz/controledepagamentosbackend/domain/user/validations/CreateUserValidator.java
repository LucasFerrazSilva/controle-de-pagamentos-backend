package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;


public interface CreateUserValidator  {
	void validator(DadosCreateUserDTO dados);
}
