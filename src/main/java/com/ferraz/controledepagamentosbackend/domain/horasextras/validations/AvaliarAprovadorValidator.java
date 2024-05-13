package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import org.springframework.stereotype.Component;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AvaliarHorasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;

@Component
public class AvaliarAprovadorValidator implements AvaliarHorasValidator{

	private UserRepository userRepository;
	private HorasExtrasRepository horasExtrasRepository;
	
	public AvaliarAprovadorValidator(UserRepository userRepository, HorasExtrasRepository horasExtrasRepository) {
		this.userRepository = userRepository;
		this.horasExtrasRepository = horasExtrasRepository;
	}
	
	@Override
	public void validate(AvaliarHorasDTO dto) {
		HorasExtras hora = horasExtrasRepository.findById(dto.id()).orElseThrow();
		User aprovador = userRepository.findById(hora.getAprovador().getId()).orElseThrow();

		if(hora.getUser().getId().equals(aprovador.getId())) {
			throw new ValidationException("Aprovador", "Usuario aprovador não habilitado para aceitar as proprias horas!");
		}
	}

}
