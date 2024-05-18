package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserService;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	@PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
	public ResponseEntity<UserDTO> criar(@RequestBody @Valid DadosCreateUserDTO dados, UriComponentsBuilder uriComponentsBuilder) {
		User user = userService.criarUsuario(dados);
		UserDTO userDTO = new UserDTO(user);
		URI uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> listar(@PageableDefault Pageable pageable, 
			@RequestParam(required = false) String nome, @RequestParam(required = false) String email,
			@RequestParam(required = false) UsuarioPerfil perfil,@RequestParam(required = false) UserStatus status) {
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
	@PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
	public ResponseEntity<UserDTO> delete(@PathVariable("id") Long id) {
		userService.deletarUsuario(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('GESTOR') || hasRole('ADMIN') || hasRole('FINANCEIRO')")
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

	@PutMapping("/mudar-senha/")
	public ResponseEntity<UserDTO> mudarSenha(@RequestBody @Valid NovaSenhaDTO dto) {
		User user = userService.mudarSenha(dto);
		UserDTO userDTO = new UserDTO(user);

		return ResponseEntity.ok(userDTO);
	}

}
