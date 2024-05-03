package com.ferraz.controledepagamentosbackend.domain.horasextras.dto;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;

import jakarta.validation.constraints.NotNull;

public class AvaliarHorasDTO {
	@NotNull
	Long id;
	@NotNull
	HorasExtrasStatus status;
}
