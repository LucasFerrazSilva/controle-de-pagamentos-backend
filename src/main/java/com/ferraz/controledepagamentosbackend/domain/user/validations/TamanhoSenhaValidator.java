package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.exceptions.SenhaInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class TamanhoSenhaValidator implements NovaSenhaValidator{

    private static final int TAMANHO_MINIMO = 12;

    @Override
    public void validate(String novaSenha) {
        if (novaSenha == null || novaSenha.length() >= TAMANHO_MINIMO){
            throw new SenhaInvalidaException("A senha precisa ter no mínimo 12 dígitos");
        }
    }
}
