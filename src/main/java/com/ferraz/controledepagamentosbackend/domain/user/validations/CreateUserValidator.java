package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.DTO.DadosUserDTO;


public interface CreateUserValidator  {
	void validator(DadosUserDTO dados);
}
