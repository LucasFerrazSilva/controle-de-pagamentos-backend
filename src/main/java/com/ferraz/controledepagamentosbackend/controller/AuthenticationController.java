package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService;
import com.ferraz.controledepagamentosbackend.infra.security.TokenService;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.TokenDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nlogi")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationService service;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var token = tokenService.generateToken((User) authentication.getPrincipal());
        var tokenDTO = new TokenDTO(token);
        return ResponseEntity.ok(tokenDTO);
    }

}
