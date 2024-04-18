package com.ferraz.controledepagamentosbackend.controller;

import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import jakarta.transaction.Transactional;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParametroControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<NovoParametroDTO> parameterDTOJackson;
    @Autowired
    private ParametroRepository parametroRepository;



    @BeforeAll
    @Transactional
    void beforeAll() {

    }

    @Test
    @DisplayName("Deve criar um parametro")
    void testCriarParametros() throws Exception {
        //Given
        NovoParametroDTO novoParametroDTO = new NovoParametroDTO("name_test", "valor_test");
        String parameterJson = parameterDTOJackson.write(novoParametroDTO).getJson();
        String endpoint = "/parametros";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(parameterJson);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }



}