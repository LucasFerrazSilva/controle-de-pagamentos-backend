package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoService;
import com.ferraz.controledepagamentosbackend.domain.notificacao.dto.NotificacaoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoDTO>> listByLoggedUser() {
        List<Notificacao> notificacoes = service.listByLoggedUser();
        List<NotificacaoDTO> notificacaoDTOList = notificacoes.stream().map(NotificacaoDTO::new).toList();
        return ResponseEntity.ok(notificacaoDTOList);
    }

    @PostMapping("/marcar-como-visualizadas")
    public ResponseEntity<Object> marcarComoVisualizadas() {
        service.marcarComoVisualizadas();
        return ResponseEntity.ok().build();
    }

}
