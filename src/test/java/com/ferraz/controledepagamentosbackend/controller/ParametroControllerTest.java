package com.ferraz.controledepagamentosbackend.controller;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.login;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParametroControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<UpdateParametroDTO> updateParameterDTOJackson;
    @Autowired
    private UserRepository userRepository;
    private HttpHeaders token;
    private static final String ENDPOINT = "/parametros";

    @BeforeAll
    @Transactional
    void beforeAll() throws Exception {
        token = login(mvc, userRepository);
    }


    @Test
    @DisplayName("Deve conseguir uma lista de parametros")
    void getAllParameters() throws Exception {
        // Given
        RequestBuilder requestBuilder = get(ENDPOINT).contentType(APPLICATION_JSON).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar um parametro")
    void getOneParameter() throws Exception {
        // Given
        String endpoint = ENDPOINT + "/1";
        RequestBuilder requestBuilder = get(endpoint).contentType(APPLICATION_JSON).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve atualizar um parametro existente")
    void updateParameter() throws Exception {
        // Given
        String endpoint = ENDPOINT + "/2";
        String novoNome = "Teste Update";
        String novoValor = "Update valor";
        UpdateParametroDTO updateParametroDTO = new UpdateParametroDTO(2l, novoNome, novoValor);
        String parameterJson = updateParameterDTOJackson.write(updateParametroDTO).getJson();
        RequestBuilder requestBuilder = put(endpoint).contentType(APPLICATION_JSON).content(parameterJson).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

}