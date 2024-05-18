package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.exceptions.SenhaInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class SenhasIguaisValidator implements NovaSenhaValidator{
    @Override
    public void validate(NovaSenhaDTO dto) {
        if (!dto.novaSenha().equals(dto.repeteSenha())){
            throw new SenhaInvalidaException("As senhas não correspondem");
        }
    }
}
