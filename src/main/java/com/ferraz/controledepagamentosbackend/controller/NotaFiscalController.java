package com.ferraz.controledepagamentosbackend.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.dropbox.core.DbxException;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalService;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notas-fiscais")
public class NotaFiscalController {

    private final NotaFiscalService service;

    public NotaFiscalController(NotaFiscalService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
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
    @PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
    public ResponseEntity<NotaFiscalDTO> update(@PathVariable("id") Long id, @RequestBody @Valid AtualizarNotaFiscalDTO dto){
        NotaFiscal notaFiscal = service.update(id, dto);
        NotaFiscalDTO notaFiscalDTO = new NotaFiscalDTO(notaFiscal);

        return ResponseEntity.ok(notaFiscalDTO);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/upload/{id}")
    public ResponseEntity<Object> upload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile multipartFile) throws IOException, DbxException {
        service.upload(id, multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Object> download(@PathVariable("id") Long id) throws DbxException {
        InputStream inputStream = service.download(id);
        NotaFiscal notaFiscal = service.findById(id);
        String[] pathSplit = notaFiscal.getFilePath().split("/");
        String fileName = pathSplit[pathSplit.length - 1];

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-disposition", "attachment; filename="+ fileName)
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Expose-Headers", "Content-Disposition, filename")
                .body(new InputStreamResource(inputStream));
    }

    @PutMapping("/marcar-como-paga/{id}")
    @PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
    public ResponseEntity<NotaFiscalDTO> marcarComoPaga(@PathVariable("id") Long id){
        NotaFiscal notaFiscal = service.marcarComoPaga(id);
        NotaFiscalDTO notaFiscalDTO = new NotaFiscalDTO(notaFiscal);

        return ResponseEntity.ok(notaFiscalDTO);
    }
}
