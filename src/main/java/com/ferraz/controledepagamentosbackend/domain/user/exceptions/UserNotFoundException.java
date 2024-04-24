package com.ferraz.controledepagamentosbackend.domain.user.exceptions;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String string) {
		super(string);
	}

}
