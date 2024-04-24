package com.ferraz.controledepagamentosbackend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserService;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosListagemUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> criar(@RequestBody @Valid DadosUserDTO dados, UriComponentsBuilder uriComponentsBuilder) {
		User user = userService.criarUsuario(dados);
		UserDTO userDTO = new UserDTO(user);
		URI uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<DadosListagemUserDTO>> listar() {
		return ResponseEntity.ok().body(userService.listarUsuarios());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<UserDTO> delete(@PathVariable("id") Long id) {
		userService.deletarUsuario(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> alterar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUserDTO dados) {
		User user = userService.alterarUsuario(id, dados);
		UserDTO userDTO = new UserDTO(user);
		return ResponseEntity.ok(userDTO);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DadosListagemUserDTO> listarUserPorId(@PathVariable Long id){
		return userService.listarUserPorId(id);
	}
}
