package com.ferraz.controledepagamentosbackend.domain.user.exceptions;

public class SenhaInvalidaException extends RuntimeException{

    public SenhaInvalidaException(String string) {
        super(string);
    }
}
