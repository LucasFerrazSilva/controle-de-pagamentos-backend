package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasService;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.HorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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

}
