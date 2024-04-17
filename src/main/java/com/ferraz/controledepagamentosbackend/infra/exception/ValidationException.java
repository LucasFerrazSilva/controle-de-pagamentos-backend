package com.ferraz.controledepagamentosbackend.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private final List<ValidationExceptionDataDTO> validationExceptionList;

    public ValidationException(String field, String message) {
        this.validationExceptionList = List.of(new ValidationExceptionDataDTO(field, message));
    }

}
