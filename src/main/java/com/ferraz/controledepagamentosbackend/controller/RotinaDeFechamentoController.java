package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.horasextras.RotinaDeFechamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rotina-de-fechamento")
public class RotinaDeFechamentoController {

    private final RotinaDeFechamentoService service;

    public RotinaDeFechamentoController(RotinaDeFechamentoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
    public ResponseEntity<Object> rotinaDeFechamento() {
        service.executarFechamento();
        return ResponseEntity.ok().build();
    }

}
