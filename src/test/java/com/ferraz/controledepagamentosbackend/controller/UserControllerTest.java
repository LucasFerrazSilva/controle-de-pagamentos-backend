package com.ferraz.controledepagamentosbackend.controller;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosAtualizacaoUserDTO;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosUserDTO;
import com.ferraz.controledepagamentosbackend.utils.TesteUtils;

import jakarta.transaction.Transactional;

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
    private UserRepository userRepository;
    
    private HttpHeaders headers;
    
    @AfterAll
    @Transactional
    void afterAll() {
    	userRepository.deleteAll();
    }
    
    @BeforeEach
    void beforeEach() throws Exception {
    	userRepository.deleteAll();
    	headers = TesteUtils.login(mvc, userRepository);
    } 
    
    @Test
    @DisplayName("Deve ser criado um usuario com as informações corretas e retornar 201 CREATED")
    void createUserSucessTest() throws Exception{
    	ObjectMapper objectMapper = new ObjectMapper();
    	String nome = "Luis";
    	String email = "Teste@teste.com.br";
    	String senha = "SenhaTeste";
    	BigDecimal salario = new BigDecimal("123.0");
    	String perfil = "ROLE_ADMIN";
    	DadosUserDTO dadosUserDTO = new DadosUserDTO(nome, email, senha, salario, perfil);
    	String jsonString = objectMapper.writeValueAsString(dadosUserDTO);
    	
    	RequestBuilder request = post(endpoint).contentType(APPLICATION_JSON).content(jsonString).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }
    
    @Test
    @DisplayName("Alterar Usuario com o email ja registrado deve retornar 400 Bad Request")
    void createUserEmailUKErrorTest() throws Exception{
      	createUser(userRepository);
    	
    	//Cria dados para ser mandado em uma requisição
    	ObjectMapper mapper = new ObjectMapper();
    	String nome = "Luis";
    	String email = "test@test.com.br";
    	String senha = "SenhaTeste";
    	BigDecimal salario = new BigDecimal("100.00");
    	String perfil = "ROLE_ADMIN";
    	
    	//Converte dados para uma DTO no corpo da requisição
    	DadosUserDTO dadosUserDTO = new DadosUserDTO(nome, email, senha, salario, perfil);
    	String jsonString = mapper.writeValueAsString(dadosUserDTO);
    	
    	//Requisicao e resposta
    	RequestBuilder builder = post(endpoint).contentType(APPLICATION_JSON).content(jsonString).headers(headers);
    	MockHttpServletResponse response = mvc.perform(builder).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve retornar 200 OK para listar usuarios")
    void listUsersTest() throws Exception {
    	RequestBuilder request = get(endpoint).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Deve deletar o usuario corretamente e retornar 204 no content")
    void deleteUserSucessTest() throws Exception {
    	User user = createUser(userRepository);
    	RequestBuilder request = delete(endpoint + "/" + user.getId()).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    @Test
    @DisplayName("Deve Retornar 404 Not Found deletando um user com id invalido")
    void deleteUserInvalidIdTest() throws Exception {
    	RequestBuilder request = delete(endpoint + "/" + 100).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    
    @Test
    @DisplayName("Deve alterar o MESMO usuario e retornar 200 OK")
    void alterarUsuarioTest() throws Exception{
    	User user = createUser(userRepository);
    	
    	//Cria DTO para ser enviado na requisicao
    	ObjectMapper mapper = new ObjectMapper();
    	String nome = "LuisTesteAlterado";
    	
    	DadosAtualizacaoUserDTO dto = new DadosAtualizacaoUserDTO(nome, null, null, null, null);
    	String jsonDto = mapper.writeValueAsString(dto);
    	
    	RequestBuilder request = put(endpoint + "/" + user.getId()).contentType(APPLICATION_JSON)
    			.content(jsonDto).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Alterar um usuario com email de outro deve retornar 400 Bad Request")
    void alterarUsuarioEmailErrorTest() throws Exception{
    	User user = createUser(userRepository);
    	
    	//Cria um usuario secundario 
    	User user2 = new User();
    	user2.setNome("Luis");
    	user2.setEmail("emailAlteravel@gmail.com");
    	user2.setSenha(new BCryptPasswordEncoder().encode("123"));
    	user2.setSalario(new BigDecimal("100.0"));
    	user2.setPerfil("ROLE_ADMIN");
    	user2.setStatus(UserStatus.ATIVO);
    	user2.setCreateDateTime(LocalDateTime.now());
    	userRepository.save(user2);

    	//Cria DTO para ser enviado na requisicao
    	ObjectMapper mapper = new ObjectMapper();
    	String email = user.getEmail();
    	
    	DadosAtualizacaoUserDTO dto = new DadosAtualizacaoUserDTO(null, email, null, null, null);
    	String jsonDto = mapper.writeValueAsString(dto);
    	RequestBuilder request = put(endpoint + "/" + user2.getId()).contentType(APPLICATION_JSON)
    			.content(jsonDto).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve listar corretamente um usuario com ID existente e retornar 200 OK")
    void listarUserByIdTest() throws Exception{
    	User user = createUser(userRepository);
    	RequestBuilder request = get(endpoint + "/" + user.getId()).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Deve retornar 404 Not Found para listar usuario com id inexistente")
    void listarUserByIdFailTest()throws Exception{
    	RequestBuilder request = get(endpoint + "/" + 0).headers(headers);
    	MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    	
    	assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    
}
