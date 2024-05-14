package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AvaliarHorasDTO;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;

@Component
public class AvaliarStatusValidator implements AvaliarHorasValidator{
	
	private HorasExtrasRepository horasRepository;
	public AvaliarStatusValidator(HorasExtrasRepository horasRepository) {
		this.horasRepository = horasRepository;
	}
	
	@Override
	public void validate(AvaliarHorasDTO dto) {
		HorasExtras hora = horasRepository.findById(dto.id()).orElseThrow();
		if(hora.getStatus() != HorasExtrasStatus.SOLICITADO) {
			throw new ValidationException("Status", "O Registro não pode ser avaliado");
		}
		
	}
}
