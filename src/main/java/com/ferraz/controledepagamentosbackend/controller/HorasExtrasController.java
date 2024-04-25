package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasService;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.HorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/horas-extras")
public class HorasExtrasController {

    private final HorasExtrasService service;

    public HorasExtrasController(HorasExtrasService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HorasExtrasDTO> create(@RequestBody @Valid NovasHorasExtrasDTO novasHorasExtrasDTO, UriComponentsBuilder uriComponentsBuilder) {
        HorasExtras horasExtras = service.create(novasHorasExtrasDTO);
        HorasExtrasDTO horasExtrasDTO = new HorasExtrasDTO(horasExtras);
        URI uri = uriComponentsBuilder.path("/horas-extras/{id}").buildAndExpand(horasExtras.getId()).toUri();
        return ResponseEntity.created(uri).body(horasExtrasDTO);
    }

    @GetMapping
    public ResponseEntity<Page<HorasExtrasDTO>> list(
            @PageableDefault Pageable pageable, @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idAprovador, @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim, @RequestParam(required = false) String descricao,
            @RequestParam(required = false) HorasExtrasStatus status) {

        Page<HorasExtras> page = service.list(pageable, idUsuario, idAprovador, dataInicio, dataFim, descricao, status);

        Page<HorasExtrasDTO> pageDTO = page.map(HorasExtrasDTO::new);
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorasExtrasDTO> findById(@PathVariable("id") Long id) {
        HorasExtras horasExtras = service.findById(id);
        HorasExtrasDTO dto = new HorasExtrasDTO(horasExtras);
        return ResponseEntity.ok(dto);
    }

}
