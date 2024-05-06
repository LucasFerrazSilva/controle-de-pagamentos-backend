package com.ferraz.controledepagamentosbackend.infra.exception;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void testAllArgsConstructor() {
        // Given
        List<ValidationExceptionDataDTO> validationExceptionList = List.of(new ValidationExceptionDataDTO("nome", "Nome invalido"));

        // When
        ValidationException exception = new ValidationException(validationExceptionList);

        // Then
        assertThat(exception.getValidationExceptionList()).hasSize(1);

    }

}