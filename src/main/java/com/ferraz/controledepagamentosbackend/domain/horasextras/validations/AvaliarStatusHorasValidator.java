package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AvaliarHorasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;

@Component
public class AvaliarStatusHorasValidator implements AvaliarHorasValidator{

	private HorasExtrasRepository extrasRepository;
	
	public AvaliarStatusHorasValidator(HorasExtrasRepository extrasRepository) {
		this.extrasRepository = extrasRepository;
	}
	@Override
	public void validate(AvaliarHorasDTO dto) {
		HorasExtras hora = extrasRepository.findById(dto.id()).orElseThrow();
		
		if(hora.getStatus().equals(HorasExtrasStatus.APROVADO) || hora.getStatus().equals(HorasExtrasStatus.RECUSADO)) {
			throw new ValidationException("Status", "A hora não pode ser aprovada ou recusada");
		}
		
	}

}
