package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroService;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.ParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid NovoParametroDTO novoParametroDTO){
        Parametro novoParametro = parametroService.save(novoParametroDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoParametro.getId())
                .toUri();

        ParametroDTO parametroDTO = new ParametroDTO(novoParametro);

        return ResponseEntity.created(location).body(parametroDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ParametroDTO>> getAll(
            @PageableDefault Pageable pageable
            ){
        Page<Parametro> parametros = parametroService.findAll(pageable);
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
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id, @RequestBody @Valid UpdateParametroDTO updateParametroDTO){
        Parametro parametro = parametroService.update(id, updateParametroDTO);
        ParametroDTO updatedParametroDTO = new ParametroDTO(parametro);
        return ResponseEntity.status(HttpStatus.OK).body(updatedParametroDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        parametroService.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
