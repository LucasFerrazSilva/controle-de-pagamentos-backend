package com.ferraz.controledepagamentosbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraz.controledepagamentosbackend.domain.user.DadosListagemUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.DadosUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private UserRepository repository;
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserController(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity criar(@RequestBody @Valid DadosUserDTO dados) {
		var user = new User(dados);
		user.setSenha(passwordEncoder.encode(dados.senha()));
		repository.save(user);
		return ResponseEntity.ok("User Criado");
	}
	
	@GetMapping
	public ResponseEntity listar() {
		var lista = repository.findAll().stream().map(DadosListagemUserDTO::new);
		return ResponseEntity.ok(lista);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity delete(@PathVariable("id") Long id) {
		repository.deleteById(id);
		return ResponseEntity.ok("Deletado");
	}
	
}
