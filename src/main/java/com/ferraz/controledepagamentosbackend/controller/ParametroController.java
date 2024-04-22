package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroService;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.ParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }


    @PostMapping
    public ResponseEntity<Object> createParameter(@RequestBody @Valid NovoParametroDTO novoParametroDTO){
        Parametro novoParametro = parametroService.save(novoParametroDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoParametro.getId())
                .toUri();

        return ResponseEntity.created(location).body(novoParametroDTO);
    }

    @GetMapping
    public ResponseEntity<List<ParametroDTO>> getAllParameters(){
        List<ParametroDTO> parametros = parametroService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(parametros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametroDTO> getOneParameter(@PathVariable(value = "id") Long id){
        ParametroDTO parametro = parametroService.findOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(parametro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParameter(@PathVariable(value = "id") Long id, @RequestBody @Valid UpdateParametroDTO updateParametroDTO){
        Parametro parametro = parametroService.update(id, updateParametroDTO);
        UpdateParametroDTO updatedParametroDTO = new UpdateParametroDTO(parametro.getId(), parametro.getNome(), parametro.getValor());
        return ResponseEntity.status(HttpStatus.OK).body(updatedParametroDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deactivateParameter(@PathVariable(value = "id") Long id){
        parametroService.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
