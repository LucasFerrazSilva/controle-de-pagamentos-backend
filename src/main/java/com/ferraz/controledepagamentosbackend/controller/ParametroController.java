package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroService;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }


    @PostMapping
    public ResponseEntity<Object> createParameter(@RequestBody @Valid NovoParametroDTO novoParametroDTO){
        Parametro novoParemetro = parametroService.save(novoParametroDTO);
        return ResponseEntity.ok(novoParemetro);
    }


}
