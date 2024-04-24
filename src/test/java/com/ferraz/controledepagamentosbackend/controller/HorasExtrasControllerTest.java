package com.ferraz.controledepagamentosbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.utils.TesteUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HorasExtrasControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<NovasHorasExtrasDTO> novasHorasExtrasDTOJacksonTester;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HorasExtrasRepository horasExtrasRepository;

    private HttpHeaders httpHeaders;

    private final String ENDPOINT = "/horas-extras";

    private User aprovador;

    @BeforeAll
    @Transactional
    void beforeAll() throws Exception {
        this.httpHeaders = TesteUtils.login(mvc, userRepository);
        this.aprovador = TesteUtils.createAprovador(userRepository);
    }

    @BeforeEach
    void beforeEach() {
        horasExtrasRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        horasExtrasRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via POST o endpoint /horas-extras passando dados válidos")
    void testCreate() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via POST o endpoint /horas-extras passando dados inválidos")
    void testCreate_DadosInvalidos() throws Exception {
        // Given
        String dadosInvalidos = "";
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosInvalidos).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /horas-extras")
    void testList() throws Exception {
        // Given
        RequestBuilder requestBuilder = get(ENDPOINT);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 404 (Not found) quando chamar via GET o endpoint /horas-extras e não houver nenhum item no banco")
    void testList_NenhumRegistro() throws Exception {
        // Given
        RequestBuilder requestBuilder = get(ENDPOINT);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isBlank();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /horas-extras passando um id valido")
    void testFindById() throws Exception {
        // Given
        Long idValido = null;
        RequestBuilder requestBuilder = get(ENDPOINT + "/" + idValido);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 404 (Not found) quando chamar via GET o endpoint /horas-extras passando um id invalido")
    void testFindById_IdInvalido() throws Exception {
        // Given
        Long idInvalido = null;
        RequestBuilder requestBuilder = get(ENDPOINT + "/" + idInvalido);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via PUT o endpoint /horas-extras passando dados e id válidos")
    void testUpdate() throws Exception {
        // Given
        String dadosValidos = "";
        Long idValido = null;
        RequestBuilder requestBuilder = put(ENDPOINT + "/" + idValido).contentType(APPLICATION_JSON).content(dadosValidos);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via PUT o endpoint /horas-extras passando dados inválidos")
    void testUpdate_DadosInvalidos() throws Exception {
        // Given
        String dadosInvalidos = "";
        Long idValido = null;
        RequestBuilder requestBuilder = put(ENDPOINT + "/" + idValido).contentType(APPLICATION_JSON).content(dadosInvalidos);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 204 (No content) quando chamar via DELETE o endpoint /horas-extras passando um id valido")
    void testDelete() throws Exception {
        // Given
        Long idValido = null;
        RequestBuilder requestBuilder = delete(ENDPOINT + "/" + idValido);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isBlank();
    }

    @Test
    @DisplayName("Deve retornar 204 (No content) quando chamar via DELETE o endpoint /horas-extras passando um id valido")
    void testDelete_IdInvalido() throws Exception {
        // Given
        Long idInvalido = null;
        RequestBuilder requestBuilder = delete(ENDPOINT + "/" + idInvalido);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

}