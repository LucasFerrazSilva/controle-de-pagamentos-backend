package com.ferraz.controledepagamentosbackend.infra.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationExceptionTest {

    @Test
    void test() {
        // Given
        String field = "nome";
        String message = "Nome invalido";

        // When
        ValidationException exception = new ValidationException(field, message);

        // Then
        assertThat(exception.getValidationExceptionList()).hasSize(1);
        assertThat(exception.getValidationExceptionList().get(0)).isEqualTo(new ValidationExceptionDataDTO(field, message));
    }

}