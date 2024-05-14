package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroService;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroStatus;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.ParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }


    @GetMapping
    public ResponseEntity<Page<ParametroDTO>> getAll(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String valor,
            @RequestParam(required = false) ParametroStatus status
            ){
        Page<Parametro> parametros = parametroService.findAll(pageable, nome, valor, status);
        Page<ParametroDTO> parametrosDTO = parametros.map(ParametroDTO::new);
        return ResponseEntity.status(HttpStatus.OK).body(parametrosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametroDTO> getOne(@PathVariable(value = "id") Long id){
        Parametro parametro = parametroService.findOne(id);
        ParametroDTO parametroDTO = new ParametroDTO(parametro);
        return ResponseEntity.status(HttpStatus.OK).body(parametroDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id, @RequestBody @Valid UpdateParametroDTO updateParametroDTO){
        Parametro parametro = parametroService.update(id, updateParametroDTO);
        ParametroDTO updatedParametroDTO = new ParametroDTO(parametro);
        return ResponseEntity.status(HttpStatus.OK).body(updatedParametroDTO);
    }

}
