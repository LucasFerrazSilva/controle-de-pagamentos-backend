package com.ferraz.controledepagamentosbackend.domain.user;

import java.util.List;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.validations.CreateUserValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.DeleteUserValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.UpdateUserValidator;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository repository;
	private final PasswordEncoder encoder;
	private final List<UpdateUserValidator> updateUserValidators;
	private final List<DeleteUserValidator> deleteUserValidators;
	private final List<CreateUserValidator> createUserValidators;

	public UserService(UserRepository repository, PasswordEncoder encoder, 
			List<UpdateUserValidator> updateUserValidators, List<DeleteUserValidator> deleteUserValidators,
			List<CreateUserValidator> createUserValidators) {
		this.createUserValidators = createUserValidators;
		this.deleteUserValidators = deleteUserValidators;
		this.updateUserValidators = updateUserValidators;
		this.repository = repository;
		this.encoder = encoder;
	}

	@Transactional
	public User criarUsuario(DadosCreateUserDTO dados) {
		createUserValidators.forEach(validator -> validator.validator(dados));
		var user = new User(dados);
		user.setSenha(encoder.encode(dados.senha()));
		repository.save(user);

		return user;
	}

	public List<UserDTO> listarUsuarios() {
		return repository.findAll().stream().map(UserDTO::new).toList();

	}

	@Transactional
	public void deletarUsuario(Long id) {
		deleteUserValidators.forEach(validator -> validator.validate(id));
		repository.deleteById(id);
	}

	@Transactional
	public User alterarUsuario(Long id, DadosAtualizacaoUserDTO dados) {	
		updateUserValidators.forEach(validator -> validator.validate(dados, id));
		User user = repository.getReferenceById(id);
		user.atualizar(dados);
		
		return user;
	}

	public User listarUserPorId(Long id) {
		User user = repository.findById(id).orElseThrow();
		return user;
	}

	
}
