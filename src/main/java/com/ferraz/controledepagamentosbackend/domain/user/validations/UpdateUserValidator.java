package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.DTO.DadosAtualizacaoUserDTO;

public interface UpdateUserValidator {

	void validate(DadosAtualizacaoUserDTO dto, Long id);
}
