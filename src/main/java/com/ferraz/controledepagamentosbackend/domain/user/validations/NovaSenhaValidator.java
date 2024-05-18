package com.ferraz.controledepagamentosbackend.domain.user.validations;

import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;

public interface NovaSenhaValidator {
    void validate(NovaSenhaDTO dto);
}
