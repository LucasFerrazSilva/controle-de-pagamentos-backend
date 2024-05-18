package com.ferraz.controledepagamentosbackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalRepository;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalStatus;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotaFiscalControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<NovaNotaFiscalDTO> novaNotaFiscalDTOJacksonTester;
    @Autowired
    private JacksonTester<AtualizarNotaFiscalDTO> atualizarNotaFiscalDTOJacksonTester;
    @Autowired
    private JacksonTester<NotaFiscalDTO> notaFiscalDTOJacksonTester;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    private HttpHeaders token;

    private final String ENDPOINT = "/notas-fiscais";

    @BeforeAll
    @Transactional
    void beforeAll() throws Exception {
        this.token = login(mvc, userRepository);
    }

    @BeforeEach
    void beforeEach() {
        notaFiscalRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        notaFiscalRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via POST o endpoint /notas-fiscais passando dados válidos")
    void testCreate() throws Exception {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        NovaNotaFiscalDTO dto = new NovaNotaFiscalDTO(
                user.getId(),
                4,
                2024,
                BigDecimal.valueOf(2000.00)
        );
        String dadosValidos = novaNotaFiscalDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /notas-fiscais")
    void list() throws Exception {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        createNotaFiscal(user, user, notaFiscalRepository);
        RequestBuilder requestBuilder = get(ENDPOINT).queryParam("status", NotaFiscalStatus.SOLICITADA.toString()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        HashMap<String,Object> page = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(page).containsEntry("numberOfElements", 1);
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /notas-fiscais passando um id válido")
    void findById() throws Exception {
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        NotaFiscal notaFiscal = createNotaFiscal(user, user, notaFiscalRepository);
        RequestBuilder requestBuilder = get(ENDPOINT + "/" + notaFiscal.getId()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        NotaFiscalDTO responseDTO = notaFiscalDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO.id()).isEqualTo(notaFiscal.getId());
        assertThat(responseDTO.userDTO().id()).isEqualTo(notaFiscal.getUser().getId());
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via PUT o endpoint /horas-extras passando dados e id válidos")
    void update() throws Exception {

        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        NotaFiscal notaFiscal = createNotaFiscal(user, user, notaFiscalRepository);
        BigDecimal novoValor = notaFiscal.getValor().add(BigDecimal.valueOf(1000));
        AtualizarNotaFiscalDTO dto = new AtualizarNotaFiscalDTO(
                user.getId(),
                notaFiscal.getMes(),
                notaFiscal.getAno(),
                novoValor
        );

        String dadosValidos = atualizarNotaFiscalDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = put(ENDPOINT + "/" + notaFiscal.getId()).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        NotaFiscalDTO responseDTO = notaFiscalDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO.id()).isEqualTo(notaFiscal.getId());
        assertThat(responseDTO.userDTO().id()).isEqualTo(notaFiscal.getUser().getId());
        assertThat(responseDTO.mes()).isEqualTo(notaFiscal.getMes());
        assertThat(responseDTO.ano()).isEqualTo(notaFiscal.getAno());

    }

    @Test
    @DisplayName("Deve retornar 204 (No content) quando chamar via DELETE o endpoint /notas-fiscais passando um id valido")
    void deleteTest() throws Exception {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        NotaFiscal notaFiscal = createNotaFiscal(user, user, notaFiscalRepository);
        RequestBuilder requestBuilder = delete(ENDPOINT + "/" + notaFiscal.getId()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Optional<NotaFiscal> optional = notaFiscalRepository.findById(notaFiscal.getId());
        assertThat(optional).isPresent();
        assertThat(optional.get().getStatus()).isEqualTo(NotaFiscalStatus.INATIVA);
    }

    @Test
    @DisplayName("Deve retornar erro se mes e ano estão no futuro")
    void testMesValidator() throws Exception {
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        NovaNotaFiscalDTO dto = new NovaNotaFiscalDTO(
                user.getId(),
                LocalDateTime.now().getMonthValue() + 1,
                2025,
                BigDecimal.valueOf(2000.00)
        );
        String dadosValidos = novaNotaFiscalDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    @DisplayName("Deve retornar erro se ano está no futuro")
    void testAnoValidator() throws Exception {
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        NovaNotaFiscalDTO dto = new NovaNotaFiscalDTO(
                user.getId(),
                LocalDateTime.now().getMonthValue(),
                2025,
                BigDecimal.valueOf(2000.00)
        );
        String dadosValidos = novaNotaFiscalDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve marcar uma nota fiscal como paga")
    void testNotaFiscalPaga() throws Exception {
        // Given
        User user = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User financeiro = createRandomUser(userRepository, UsuarioPerfil.ROLE_FINANCEIRO);
        NotaFiscal notaFiscal = createNotaFiscal(user, financeiro, notaFiscalRepository);
        RequestBuilder requestBuilder = put(ENDPOINT + "/marcar-como-paga/" + notaFiscal.getId()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        NotaFiscalDTO dto = notaFiscalDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(dto.status()).isEqualTo(NotaFiscalStatus.PAGA);

    }

}