package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AvaliarHorasDTO;

@Component
public class AvaliarRolesValidator implements AvaliarHorasValidator{

	private HorasExtrasRepository extrasRepository;
	
	public AvaliarRolesValidator(HorasExtrasRepository extrasRepository) {
		this.extrasRepository = extrasRepository;
	}
	
	@Override
	public void validate(AvaliarHorasDTO dto) {
		HorasExtras hora = extrasRepository.findById(dto.id()).orElseThrow();
	}

}
