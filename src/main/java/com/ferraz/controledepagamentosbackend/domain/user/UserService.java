package com.ferraz.controledepagamentosbackend.domain.user;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

import java.security.SecureRandom;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferraz.controledepagamentosbackend.domain.emails.EnviarCredenciaisEmail;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.validations.CreateUserValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.DeleteUserValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.NovaSenhaValidator;
import com.ferraz.controledepagamentosbackend.domain.user.validations.UpdateUserValidator;
import com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	private final long PARAMETRO_ENVIO_EMAIL = 6;
	
	@Value("${senha.default}")
	public String default_password;
	
	
	@Value("${application_sender}")
    private String applicationSender;

	private final ParametroRepository parametroRepository;
	private final UserRepository repository;
	private final PasswordEncoder encoder;
	private final List<UpdateUserValidator> updateUserValidators;
	private final List<DeleteUserValidator> deleteUserValidators;
	private final List<CreateUserValidator> createUserValidators;
	private final EnviarCredenciaisEmail enviarCredenciaisEmail;
	private final List<NovaSenhaValidator> novaSenhaValidators;

	public UserService(UserRepository repository, PasswordEncoder encoder,
                       List<UpdateUserValidator> updateUserValidators, List<DeleteUserValidator> deleteUserValidators,
                       List<CreateUserValidator> createUserValidators, List<NovaSenhaValidator> novaSenhaValidators, 
                       EnviarCredenciaisEmail enviarCredenciaisEmail, ParametroRepository parametroRepository) {
		this.parametroRepository = parametroRepository;
		this.enviarCredenciaisEmail = enviarCredenciaisEmail;
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
		String randomPassword = gerarSenhaAleatoria();

		var user = new User(dados);
		
		if(deveEnviarEmailDeCredenciais()) {
			user.setSenha(encoder.encode(randomPassword));
			repository.save(user);
			enviarCredenciaisEmail.enviarCredenciaisEmail(user, randomPassword);
			return user;
		}
		
		user.setSenha(encoder.encode(default_password));
		repository.save(user);
		return user;
		
	}

	public Page<User> listarUsuarios(Pageable pageable, String nome, String email, UsuarioPerfil perfil,
			UserStatus status) {
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

	
	private boolean deveEnviarEmailDeCredenciais() {
        return parametroRepository
                        .findById(PARAMETRO_ENVIO_EMAIL)
                        .map(parametro -> "S".equals(parametro.getValor()))
                        .orElse(false);
    }
	
	private String gerarSenhaAleatoria() {
		String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "@#$%^&+=_";
        String allChars = upperCase + lowerCase + digits + specialChars;

        SecureRandom random = new SecureRandom();
        String password = random.ints(10, 0, allChars.length())
                                .mapToObj(allChars::charAt)
                                .map(String::valueOf)
                                .collect(Collectors.joining());

		return password;
}


	@Transactional
	public User mudarSenha(NovaSenhaDTO dto) {
		novaSenhaValidators.forEach(validator -> validator.validate(dto));
		User user = repository.findById(getLoggedUser().getId()).orElseThrow();
		user.mudarSenha(encoder.encode(dto.novaSenha()), user);
		repository.save(user);

		return user;
	}
}
