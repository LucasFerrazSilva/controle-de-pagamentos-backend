package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.exceptions.SenhaInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class TamanhoSenhaValidator implements NovaSenhaValidator{

    private static final int TAMANHO_MINIMO = 8;

    @Override
    public void validate(NovaSenhaDTO dto) {
        if (dto.novaSenha() == null || dto.novaSenha().length() < TAMANHO_MINIMO){
            throw new SenhaInvalidaException("A senha precisa ter no mínimo 8 dígitos");
        }
    }
}
