package com.ferraz.controledepagamentosbackend.controller;

import java.net.URI;
import java.util.List;

import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserService;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
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
	public ResponseEntity<UserDTO> criar(@RequestBody @Valid DadosCreateUserDTO dados, UriComponentsBuilder uriComponentsBuilder) {
		User user = userService.criarUsuario(dados);
		UserDTO userDTO = new UserDTO(user);
		URI uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> listar(@PageableDefault Pageable pageable, 
			@RequestParam(required = false) String nome, @RequestParam(required = false) String email,
			@RequestParam(required = false) String perfil,@RequestParam(required = false) UserStatus status) {
		Page<User> users = userService.listarUsuarios(pageable, nome, email, perfil,status);
		Page<UserDTO> pageDTO = users.map(UserDTO::new);
		return ResponseEntity.ok().body(pageDTO);
	}

	@GetMapping("/listar-por-perfil/{perfil}")
	public ResponseEntity<List<UserDTO>> listarPorPerfil(@PathVariable UsuarioPerfil perfil) {
		List<User> users = userService.listarPorPerfil(perfil);
		List<UserDTO> usersDTOs = users.stream().map(UserDTO::new).toList();
		return ResponseEntity.ok(usersDTOs);
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
	public ResponseEntity<UserDTO> listarUserPorId(@PathVariable Long id){
		User user = userService.listarUserPorId(id);
		UserDTO dto = new UserDTO(user);
		return ResponseEntity.ok().body(dto);
	}
}
