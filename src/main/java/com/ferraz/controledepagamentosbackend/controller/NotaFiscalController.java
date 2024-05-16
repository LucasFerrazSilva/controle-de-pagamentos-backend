package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalService;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/notas-fiscais")
public class NotaFiscalController {

    private final NotaFiscalService service;

    public NotaFiscalController(NotaFiscalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotaFiscalDTO> create(@RequestBody @Valid NovaNotaFiscalDTO dto, UriComponentsBuilder uriComponentsBuilder){
        NotaFiscal notaFiscal = service.create(dto);
        NotaFiscalDTO notaFiscalDTO = new NotaFiscalDTO(notaFiscal);
        URI uri = uriComponentsBuilder.path("/notas-fiscais/{id}").buildAndExpand(notaFiscal.getId()).toUri();
        return ResponseEntity.created(uri).body(notaFiscalDTO);
    }

    @GetMapping
    public ResponseEntity<Page<NotaFiscalDTO>> list(
            @PageableDefault Pageable pageable, @RequestParam(required = false) Integer mes, @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Integer ano, @RequestParam(required = false) BigDecimal valor,
            @RequestParam(required = false) NotaFiscalStatus status){

        Page<NotaFiscal> page = this.service.list(pageable, idUsuario, mes, ano, valor, status);

        Page<NotaFiscalDTO> pageDTO = page.map(NotaFiscalDTO::new);
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaFiscalDTO> findById(@PathVariable("id") Long id) {
        NotaFiscal notaFiscal = service.findById(id);
        NotaFiscalDTO dto = new NotaFiscalDTO(notaFiscal);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaFiscalDTO> update(@PathVariable("id") Long id, @RequestBody @Valid AtualizarNotaFiscalDTO dto){
        NotaFiscal notaFiscal = service.update(id, dto);
        NotaFiscalDTO notaFiscalDTO = new NotaFiscalDTO(notaFiscal);

        return ResponseEntity.ok(notaFiscalDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
