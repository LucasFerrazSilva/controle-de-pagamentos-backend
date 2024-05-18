package com.ferraz.controledepagamentosbackend.controller;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.createRandomUser;
import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.login;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametros;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UserService;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.NovaSenhaDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
	private final String endpoint = "/user";
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ParametroRepository parametroRepository;
    

	@Autowired
	private JacksonTester<List<UserDTO>> userDtoListJackson;
    
    private HttpHeaders token;

	@Autowired
	private JacksonTester<NovaSenhaDTO> novaSenhaDTOJacksonTester;

	@Autowired
	JacksonTester<UserDTO> userDTOJacksonTester;

	@Autowired
	private PasswordEncoder encoder;



    @BeforeAll
    void beforeAll() throws Exception {

    	token = login(mvc, userRepository);
    }
    
    @Test
    @DisplayName("Deve ser criado um usuario com as informações corretas e retornar 201 CREATED")
    void createUserSucessTest() throws Exception{
    	ObjectMapper objectMapper = new ObjectMapper();
    	String nome = "Luis";
    	String email = "Teste@teste.com.br";
    	BigDecimal salario = new BigDecimal("123.0");
		UsuarioPerfil perfil = UsuarioPerfil.ROLE_ADMIN;
    	DadosCreateUserDTO dadosUserDTO = new DadosCreateUserDTO(nome, email, salario, perfil);
    	String jsonString = objectMapper.writeValueAsString(dadosUserDTO);
    	
    	RequestBuilder request = post(endpoint).contentType(APPLICATION_JSON).content(jsonString).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }
    
    @Test
    @DisplayName("Alterar Usuario com o email ja registrado deve retornar 400 Bad Request")
    void createUserEmailUKErrorTest() throws Exception{
		User randomUser = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);

		//Cria dados para ser mandado em uma requisição
    	ObjectMapper mapper = new ObjectMapper();
    	String nome = "Luis";
    	String email = randomUser.getEmail();
    	BigDecimal salario = new BigDecimal("100.00");
		UsuarioPerfil perfil = UsuarioPerfil.ROLE_ADMIN;
    	
    	//Converte dados para uma DTO no corpo da requisição
    	DadosCreateUserDTO dadosUserDTO = new DadosCreateUserDTO(nome, email, salario, perfil);
    	String jsonString = mapper.writeValueAsString(dadosUserDTO);
    	
    	//Requisicao e resposta
    	RequestBuilder builder = post(endpoint).contentType(APPLICATION_JSON).content(jsonString).headers(token);
    	MockHttpServletResponse response = mvc.perform(builder).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve retornar 200 OK para listar usuarios")
    void listUsersTest() throws Exception {
    	RequestBuilder request = get(endpoint).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Deve deletar o usuario corretamente e retornar 204 no content")
    void deleteUserSucessTest() throws Exception {
    	User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
    	RequestBuilder request = delete(endpoint + "/" + user.getId()).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    @Test
    @DisplayName("Deve Retornar 404 Not Found deletando um user com id invalido")
    void deleteUserInvalidIdTest() throws Exception {
    	RequestBuilder request = delete(endpoint + "/" + 100).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    
    @Test
    @DisplayName("Deve alterar o MESMO usuario e retornar 200 OK")
    void alterarUsuarioTest() throws Exception{
    	User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
    	
    	//Cria DTO para ser enviado na requisicao
    	ObjectMapper mapper = new ObjectMapper();
    	String nome = "LuisTesteAlterado";
    	
    	DadosAtualizacaoUserDTO dto = new DadosAtualizacaoUserDTO(nome, null, null, null);
    	String jsonDto = mapper.writeValueAsString(dto);
    	
    	RequestBuilder request = put(endpoint + "/" + user.getId()).contentType(APPLICATION_JSON)
    			.content(jsonDto).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Alterar um usuario com email de outro deve retornar 400 Bad Request")
    void alterarUsuarioEmailErrorTest() throws Exception{
    	User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
    	
    	//Cria um usuario secundario 
    	User user2 = new User();
    	user2.setNome("Luis");
    	user2.setEmail("emailAlteravel@gmail.com");
    	user2.setSenha(new BCryptPasswordEncoder().encode("123"));
    	user2.setSalario(new BigDecimal("100.0"));
    	user2.setPerfil(UsuarioPerfil.ROLE_ADMIN);
    	user2.setStatus(UserStatus.ATIVO);
    	user2.setCreateDateTime(LocalDateTime.now());
    	userRepository.save(user2);

    	//Cria DTO para ser enviado na requisicao
    	ObjectMapper mapper = new ObjectMapper();
    	String email = user.getEmail();
    	
    	DadosAtualizacaoUserDTO dto = new DadosAtualizacaoUserDTO(null, email, null, null);
    	String jsonDto = mapper.writeValueAsString(dto);
    	RequestBuilder request = put(endpoint + "/" + user2.getId()).contentType(APPLICATION_JSON)
    			.content(jsonDto).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve listar corretamente um usuario com ID existente e retornar 200 OK")
    void listarUserByIdTest() throws Exception{
    	User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
    	RequestBuilder request = get(endpoint + "/" + user.getId()).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Deve retornar 404 Not Found para listar usuario com id inexistente")
    void listarUserByIdFailTest()throws Exception{
    	RequestBuilder request = get(endpoint + "/" + 0).headers(token);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

	@Test
	@DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /listar-por-perfil passando um perfil valido")
	void testListarPorPerfil() throws Exception {
		// Given
		createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
		createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);

		// When
		RequestBuilder request = get(endpoint + "/listar-por-perfil/" + UsuarioPerfil.ROLE_USER).headers(token);
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isNotBlank();
	}
	
	@Test
	@DisplayName("Deve retornar 201 (CREATED) com a senha aleatoria se parametro 6 Valor == 'S' ")
	void loginParametroCredenciaisAtivo() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		
		Parametro parametroEnvioCredenciais = parametroRepository.
				findById(Parametros.DEVE_ENVIAR_CREDENCIAIS_DE_ACESSO.getId()).orElseThrow();
		parametroEnvioCredenciais.setValor("S");
		parametroRepository.save(parametroEnvioCredenciais);
		
		User gestor = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
		HttpHeaders loginGestor = login(mvc, gestor);
		
		DadosCreateUserDTO dto = new DadosCreateUserDTO("Luis Gustavo Vanique", "cunhagustavo142@gmail.com", 
				new BigDecimal("12321"), UsuarioPerfil.ROLE_USER);
		String jsonDTO = mapper.writeValueAsString(dto);
		RequestBuilder request = post(endpoint).contentType(APPLICATION_JSON).content(jsonDTO).headers(loginGestor);
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
		
		User user = userRepository.findByEmail(dto.email());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(new BCryptPasswordEncoder().matches(userService.default_password, user.getSenha())).isFalse();
		assertThat(user).isNotNull();
	}
	
	@Test
	@DisplayName("Deve retornar 201 (CREATED) e com a senha default se Parametro id 6 == 'N' ")
	void loginParametroCredenciaisInativo() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		
		Parametro parametroEnvioCredenciais = parametroRepository.
				findById(Parametros.DEVE_ENVIAR_CREDENCIAIS_DE_ACESSO.getId()).orElseThrow();
		parametroEnvioCredenciais.setValor("N");
		parametroRepository.save(parametroEnvioCredenciais);
		
		User gestor = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
		HttpHeaders loginGestor = login(mvc, gestor);
		
		DadosCreateUserDTO dto = new DadosCreateUserDTO("Luis Gustavo Vanique", "luisvanique@gmail.com", 
				new BigDecimal("12321"), UsuarioPerfil.ROLE_USER);
		String jsonDTO = mapper.writeValueAsString(dto);
		RequestBuilder request = post(endpoint).contentType(APPLICATION_JSON).content(jsonDTO).headers(loginGestor);
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
		
		User user = userRepository.findByEmail(dto.email());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(new BCryptPasswordEncoder().matches(userService.default_password, user.getSenha())).isTrue();
		assertThat(user).isNotNull();
	}

	@Test
	@DisplayName("Deve mudar a senha de um usuário")
	void testMudarSenha() throws Exception {
		// Given
		User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
		HttpHeaders randomUserToken = login(mvc, user);
		String novaSenha = "Nova_senha123";
		RequestBuilder request = montaHeadersTrocarSenha(novaSenha, randomUserToken);

		// When
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
		User novaSenhaUser = userRepository.findById(userDTOJacksonTester.parse(response.getContentAsString()).getObject().id()).orElseThrow();
		// Then
		assertThat(encoder.matches(novaSenha, novaSenhaUser.getSenha())).isTrue();
	}

	@Test
	@DisplayName("Deve retornar erro por senha ser muito fraca")
	void testMudarSenhaFraca() throws Exception {
		// Given
		String novaSenha = "senha_fraca";
		RequestBuilder request = montaHeadersTrocarSenha(novaSenha, token);

		// When
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Then

		assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Test
	@DisplayName("Deve retornar erro de senha muito curta")
	void testMudarSenhaCurta() throws Exception {
		// Given
		String novaSenha = "Senha@1";
		RequestBuilder request = montaHeadersTrocarSenha(novaSenha, token);

		// When
		MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

		// Then

		assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private RequestBuilder montaHeadersTrocarSenha(String novaSenha, HttpHeaders token) throws Exception {
		NovaSenhaDTO dto = new NovaSenhaDTO(novaSenha, novaSenha);
		String jsonDto = novaSenhaDTOJacksonTester.write(dto).getJson();

        return put(endpoint + "/mudar-senha").contentType(APPLICATION_JSON)
				.content(jsonDto).headers(token);
	}
}
