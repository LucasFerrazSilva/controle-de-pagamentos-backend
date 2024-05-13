package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasService;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AvaliarHorasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.HorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @PageableDefault(sort="dataHoraFim", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idAprovador, @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim, @RequestParam(required = false) String descricao,
            @RequestParam HorasExtrasStatus status) {

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

    @PutMapping("/{id}")
    public ResponseEntity<HorasExtrasDTO> update(@PathVariable("id") Long id, @RequestBody @Valid AtualizarHorasExtrasDTO atualizarHorasExtrasDTO) {
        HorasExtras horasExtras = service.update(id, atualizarHorasExtrasDTO);
        HorasExtrasDTO responseDTO = new HorasExtrasDTO(horasExtras);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/avaliar-horas")
    public ResponseEntity<Object> avaliarHoras(@RequestBody @Valid AvaliarHorasDTO dados){
    	HorasExtras hora = service.avaliarHora(dados);
    	HorasExtrasDTO horasDTO = new HorasExtrasDTO(hora);
		return ResponseEntity.ok(horasDTO);
    }


}
