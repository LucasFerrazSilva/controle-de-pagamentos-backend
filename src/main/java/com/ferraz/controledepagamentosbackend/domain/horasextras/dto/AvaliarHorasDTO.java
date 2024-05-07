package com.ferraz.controledepagamentosbackend.domain.horasextras.dto;

import java.time.LocalDateTime;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;

import jakarta.validation.constraints.NotNull;

public record AvaliarHorasDTO(
		@NotNull 
		Long id, 
		@NotNull 
		HorasExtrasStatus status
	) {
}