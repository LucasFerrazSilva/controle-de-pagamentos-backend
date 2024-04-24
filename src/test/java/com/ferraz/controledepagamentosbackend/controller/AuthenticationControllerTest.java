package com.ferraz.controledepagamentosbackend.controller;

import static com.ferraz.controledepagamentosbackend.domain.user.UserStatus.ATIVO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<AuthenticationDTO> authenticationDTOJackson;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    @Transactional
    void beforeAll() {
        Long id = 1L;
        String email = "teste@teste.com";
        String password = "password";
        String name = "Nome Teste";
        this.user = new User(id, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), "ROLE_ADMIN", ATIVO, LocalDateTime.now(), null, null, null);
        userRepository.save(user);
    }

    @AfterAll
    @Transactional
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar /login passando credenciais válidas")
    void testValidLogin() throws Exception {
        // Given
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(user.getEmail(), "password");
        String credentials = authenticationDTOJackson.write(authenticationDTO).getJson();
        String endpoint = "/login";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(credentials);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando chamar /login passando credenciais inválidas")
    void testInvalidLogin_InvalidCredentials() throws Exception {
        // Given
        String email = "email_invalido@teste.com";
        String password = "senha_invalida";
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(email, password);
        String credentials = authenticationDTOJackson.write(authenticationDTO).getJson();
        String endpoint = "/login";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(credentials);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains("Credenciais inválidas");
    }

}