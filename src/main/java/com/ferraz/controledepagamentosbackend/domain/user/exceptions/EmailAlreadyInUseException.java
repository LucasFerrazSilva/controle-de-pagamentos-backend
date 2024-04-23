package com.ferraz.controledepagamentosbackend.domain.user.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyInUseException(String msg) {
		super(msg);
	}

}
