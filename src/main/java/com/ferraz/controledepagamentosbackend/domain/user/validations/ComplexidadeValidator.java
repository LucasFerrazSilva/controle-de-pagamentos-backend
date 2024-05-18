package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.exceptions.SenhaInvalidaException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ComplexidadeValidator implements NovaSenhaValidator{

    private static final Pattern COMPLEXITY_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{12,}$");

    @Override
    public void validate(NovaSenhaDTO dto) {
        if (!COMPLEXITY_PATTERN.matcher(dto.novaSenha()).matches()){
            throw new SenhaInvalidaException("Senha muito fraca");
        }
    }
}
