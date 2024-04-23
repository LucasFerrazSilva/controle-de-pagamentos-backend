package com.ferraz.controledepagamentosbackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.parameters.Parametro;
import com.ferraz.controledepagamentosbackend.domain.parameters.ParametroRepository;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.ParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.ferraz.controledepagamentosbackend.domain.user.UserStatus.ATIVO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParametroControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<NovoParametroDTO> novoParametroDTOJackson;

    @Autowired
    private JacksonTester<ParametroDTO> parameterDTOJackson;

    @Autowired
    private JacksonTester<UpdateParametroDTO> updateParameterDTOJackson;

    @Autowired
    private ParametroRepository parametroRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private HttpHeaders httpHeaders;

    private Parametro parametro1;

    int contador;

    @AfterAll
    @Transactional
    void afterAll() {
        parametroRepository.deleteAll();
    }

    @BeforeAll
    @Transactional
    void beforeAll() throws Exception {
        contador = 0;
        Long id = 1L;
        String email = "teste@teste.com";
        String password = TesteUtils.DEFAULT_PASSWORD;
        String name = "Nome Teste";
        this.user = new User(id, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), "ROLE_ADMIN", ATIVO, LocalDateTime.now(), null, null, null);

        this.user = userRepository.save(user);
        httpHeaders = TesteUtils.login(mvc, this.user);

    }

    @BeforeEach
    @Transactional
    void beforeEach(){

        NovoParametroDTO novoParametroDTO1 = new NovoParametroDTO("parametro_" + contador, "valor_1");
        parametro1 = parametroRepository.save(new Parametro(novoParametroDTO1, this.user));

        NovoParametroDTO novoParametroDTO2 = new NovoParametroDTO("parametro_2", "valor_2");
        parametroRepository.save(new Parametro(novoParametroDTO2, this.user));

        NovoParametroDTO novoParametroDTO3 = new NovoParametroDTO("parametro_3", "valor_3");
        parametroRepository.save(new Parametro(novoParametroDTO3, this.user));
    }

    @AfterEach
    @Transactional
    void afterEach(){
        contador++;
        parametroRepository.deleteAll();
    }
    @Test
    @DisplayName("Deve criar um parametro")
    void testCriarParametros() throws Exception {
        //Given

        NovoParametroDTO novoParametroDTO = new NovoParametroDTO("name_test", "valor_test");
        String parameterJson = novoParametroDTOJackson.write(novoParametroDTO).getJson();
        String endpoint = "/parametros";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(parameterJson).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Deve retornar erro por não conseguir criar parametro")
    void testCriarParametros_ErroAtributosNulos() throws Exception {
        //Given
        NovoParametroDTO novoParametroDTO = new NovoParametroDTO(null, null);
        String parameterJson = novoParametroDTOJackson.write(novoParametroDTO).getJson();
        String endpoint = "/parametros";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(parameterJson).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("Deve conseguir uma lista de parametros")
    void getAllParameters() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();

        String endpoint = "/parametros";
        RequestBuilder requestBuilder = get(endpoint).contentType(APPLICATION_JSON).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        List<ParametroDTO> parametroList = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<ParametroDTO>>(){});
        assertThat(parametroList).hasSize(3);
        assertThat(parametroList.get(0).nome()).isEqualTo("parametro_" + contador);
        assertThat(parametroList.get(1).valor()).isEqualTo("valor_2");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deve retornar um parametro")
    void getOneParameter() throws Exception {
        // Given
        String endpoint = "/parametros/" + parametro1.getId();
        RequestBuilder requestBuilder = get(endpoint).contentType(APPLICATION_JSON).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        ParametroDTO parametroDTO = parameterDTOJackson.parse(response.getContentAsString()).getObject();
        assertThat(parametroDTO).isNotNull();
        assertThat(parametroDTO.valor()).isEqualTo("valor_1");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deve atualizar um parametro existente")
    void updateParameter() throws Exception {
        // Given
        String endpoint = "/parametros/" + parametro1.getId();
        String novoNome = "Teste Update";
        String novoValor = "Update valor";
        UpdateParametroDTO updateParametroDTO = new UpdateParametroDTO(1L, novoNome, novoValor);
        String parameterJson = updateParameterDTOJackson.write(updateParametroDTO).getJson();
        RequestBuilder requestBuilder = put(endpoint).contentType(APPLICATION_JSON).content(parameterJson).headers(httpHeaders);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        UpdateParametroDTO updatedParameter = updateParameterDTOJackson.parse(response.getContentAsString()).getObject();
        assertThat(updatedParameter.id()).isEqualTo(parametro1.getId());
        assertThat(updatedParameter.nome()).isEqualTo(novoNome);
        assertThat(updatedParameter.valor()).isEqualTo(novoValor);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deve desativar um parametro e retornar NO_CONTENT")
    void deactivateParameter() throws Exception {
        // Given
        String endpoint = "/parametros/" + parametro1.getId();
        RequestBuilder requestBuilder = delete(endpoint).contentType(APPLICATION_JSON).headers(httpHeaders);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}