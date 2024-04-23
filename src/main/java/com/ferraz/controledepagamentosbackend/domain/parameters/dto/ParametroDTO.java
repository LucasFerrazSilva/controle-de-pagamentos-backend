package com.ferraz.controledepagamentosbackend.domain.parameters.dto;

import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;

public record ParametroDTO (

        Long id,
        String nome,
        String valor
) {
    public ParametroDTO(Parametro parametro){
        this(parametro.getId(), parametro.getNome(), parametro.getValor());
    }
}
