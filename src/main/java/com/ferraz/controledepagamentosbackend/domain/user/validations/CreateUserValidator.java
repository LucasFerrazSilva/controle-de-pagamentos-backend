package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosUserDTO;


public interface CreateUserValidator  {
	void validator(DadosUserDTO dados);
}
