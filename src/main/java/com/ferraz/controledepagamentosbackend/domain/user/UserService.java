package com.ferraz.controledepagamentosbackend.domain.user;

import java.util.List;
import java.util.NoSuchElementException;

import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.validations.NovaSenhaValidator;
import com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.validations.CreateUserValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.DeleteUserValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.UpdateUserValidator;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository repository;
	private final PasswordEncoder encoder;
	private final List<UpdateUserValidator> updateUserValidators;
	private final List<DeleteUserValidator> deleteUserValidators;
	private final List<CreateUserValidator> createUserValidators;
	private final List<NovaSenhaValidator> novaSenhaValidators;

	public UserService(UserRepository repository, PasswordEncoder encoder,
                       List<UpdateUserValidator> updateUserValidators, List<DeleteUserValidator> deleteUserValidators,
                       List<CreateUserValidator> createUserValidators, List<NovaSenhaValidator> novaSenhaValidators) {
		this.createUserValidators = createUserValidators;
		this.deleteUserValidators = deleteUserValidators;
		this.updateUserValidators = updateUserValidators;
		this.novaSenhaValidators = novaSenhaValidators;
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

	public Page<User> listarUsuarios(Pageable pageable, String nome, String email, UsuarioPerfil perfil, UserStatus status) {
		return repository.findByFiltros(pageable, nome, email, perfil, status);

	}

	@Transactional
	public void deletarUsuario(Long id) {
		deleteUserValidators.forEach(validator -> validator.validate(id));
		User user = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
		user.deactivate(AuthenticationService.getLoggedUser());
		repository.save(user);
	}

	@Transactional
	public User alterarUsuario(Long id, DadosAtualizacaoUserDTO dados) {	
		updateUserValidators.forEach(validator -> validator.validate(dados, id));
		User user = repository.getReferenceById(id);
		user.atualizar(dados);
		return user;
	}

	public User listarUserPorId(Long id) {
		return repository.findById(id).orElseThrow();
	}

    public List<User> listarPorPerfil(UsuarioPerfil usuarioPerfil) {
		return repository.findByPerfilAndStatusOrderByNome(usuarioPerfil, UserStatus.ATIVO);
	}

	@Transactional
	public User mudarSenha(NovaSenhaDTO dto) throws BadRequestException {
		novaSenhaValidators.forEach(validator -> validator.validate(dto));
		User user = getLoggedUser();
        if (user == null) {
			throw new BadRequestException();
		}
		user.mudarSenha(encoder.encode(dto.novaSenha()), user);
		repository.save(user);

		return user;

	}
}
