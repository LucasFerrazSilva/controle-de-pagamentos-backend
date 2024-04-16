package com.ferraz.controledepagamentosbackend.infra.exception;

import org.springframework.validation.FieldError;

public record ValidationExceptionDataDTO (
    String field,
    String message
) {
    public ValidationExceptionDataDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
