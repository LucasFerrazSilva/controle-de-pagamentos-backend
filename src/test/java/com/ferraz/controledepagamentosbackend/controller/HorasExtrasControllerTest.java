package com.ferraz.controledepagamentosbackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.HorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.link.AcaoLink;
import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.domain.link.LinkRepository;
import com.ferraz.controledepagamentosbackend.domain.link.LinkStatus;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ferraz.controledepagamentosbackend.utils.TesteUtils.*;
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
    private JacksonTester<AtualizarHorasExtrasDTO> atualizarHorasExtrasDTOJacksonTester;
    @Autowired
    private JacksonTester<HorasExtrasDTO> horasExtrasDTOJacksonTester;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HorasExtrasRepository horasExtrasRepository;
    @Autowired
    private LinkRepository linkRepository;

    private HttpHeaders token;

    private final String ENDPOINT = "/horas-extras";

    private User aprovador;


    @BeforeAll
    @Transactional
    void beforeAll() throws Exception {
        this.token = login(mvc, userRepository);
        this.aprovador = TesteUtils.createAprovador(userRepository);
    }

    @BeforeEach
    void beforeEach() {
        linkRepository.deleteAll();
        horasExtrasRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        linkRepository.deleteAll();
        horasExtrasRepository.deleteAll();
        userRepository.deleteAll();
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
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad Request) quando chamar via POST o endpoint /horas-extras passando data/hora ja utilizada")
    void testCreate_DataHoraOcupada() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        mvc.perform(requestBuilder).andReturn().getResponse();

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via POST o endpoint /horas-extras passando dados inválidos")
    void testCreate_DadosInvalidos() throws Exception {
        // Given
        String dadosInvalidos = "";
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosInvalidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via POST o endpoint /horas-extras passando usuario e aprovador iguais")
    void testCreate_UsuarioEAprovadorIguais() throws Exception {
        // Given
        HttpHeaders aprovadorToken = login(mvc, aprovador);
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(aprovadorToken);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via POST o endpoint /horas-extras passando data/hora inicio posterior a data/hora fim")
    void testCreate_DataInicioPosteriorDataFim() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now().plusHours(4),
                LocalDateTime.now(),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad Request) quando chamar via POST o endpoint /horas-extras passando um aprovador que nao tem perfil para aprovar")
    void testCreate_AprovadorNaoTemPerfilNecessario() throws Exception {
        // Given
        User user = TesteUtils.createUser(userRepository, UsuarioPerfil.ROLE_USER);
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                user.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

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
        createHorasExtras(aprovador, horasExtrasRepository);
        RequestBuilder requestBuilder = get(ENDPOINT).queryParam("status", HorasExtrasStatus.SOLICITADO.toString()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        HashMap<String,Object> page = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(page).containsEntry("numberOfElements", 1);
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /horas-extras, mas so retornar as horas extras do usuario logado")
    void testList_SoRetornarHorasExtrasDoUsuarioLogado() throws Exception {
        // Given
        User user1 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);
        User user2 = createRandomUser(userRepository, UsuarioPerfil.ROLE_USER);

        createHorasExtras(user1, aprovador, horasExtrasRepository);
        createHorasExtras(user2, aprovador, horasExtrasRepository);

        HttpHeaders token = login(mvc, user1);

        // When
        RequestBuilder requestBuilder = get(ENDPOINT).queryParam("status", HorasExtrasStatus.SOLICITADO.toString()).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        HashMap<String,Object> page = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(page).containsEntry("numberOfElements", 1);
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /horas-extras")
    void testList_ComDatas() throws Exception {
        // Given
        createHorasExtras(aprovador, horasExtrasRepository);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dataInicio", LocalDate.now().minusDays(1).toString());
        params.add("dataFim", LocalDate.now().plusDays(1).toString());
        params.add("status", HorasExtrasStatus.SOLICITADO.toString());
        RequestBuilder requestBuilder = get(ENDPOINT).queryParams(params).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        HashMap<String,Object> page = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(page).containsEntry("numberOfElements", 1);
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via GET o endpoint /horas-extras passando um id valido")
    void testFindById() throws Exception {
        // Given
        HorasExtras horasExtras = createHorasExtras(aprovador, horasExtrasRepository);
        RequestBuilder requestBuilder = get(ENDPOINT + "/" + horasExtras.getId()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        HorasExtrasDTO responseDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO.id()).isEqualTo(horasExtras.getId());
        assertThat(responseDTO.user().id()).isEqualTo(horasExtras.getUser().getId());
    }

    @Test
    @DisplayName("Deve retornar 404 (Not found) quando chamar via GET o endpoint /horas-extras passando um id invalido")
    void testFindById_IdInvalido() throws Exception {
        // Given
        createHorasExtras(aprovador, horasExtrasRepository);
        RequestBuilder requestBuilder = get(ENDPOINT + "/" + 99999).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando chamar via PUT o endpoint /horas-extras passando dados e id válidos")
    void testUpdate() throws Exception {
        // Given
        User randomUser = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
        HorasExtras horasExtras = createHorasExtras(aprovador, horasExtrasRepository);
        AtualizarHorasExtrasDTO dto = new AtualizarHorasExtrasDTO(
                horasExtras.getDataHoraInicio().minusHours(1),
                horasExtras.getDataHoraFim().plusHours(1),
                "Nova descricao",
                randomUser.getId()
                    );
        String dadosValidos = atualizarHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = put(ENDPOINT + "/" + horasExtras.getId()).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        HorasExtrasDTO responseDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO.id()).isEqualTo(horasExtras.getId());
        assertThat(responseDTO.user().id()).isEqualTo(horasExtras.getUser().getId());
        assertThat(responseDTO.dataHoraInicio()).isEqualTo(dto.dataHoraInicio());
        assertThat(responseDTO.dataHoraFim()).isEqualTo(dto.dataHoraFim());
        assertThat(responseDTO.descricao()).isEqualTo(dto.descricao());
        assertThat(responseDTO.aprovador().id()).isEqualTo(dto.idAprovador());
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via PUT o endpoint /horas-extras passando dados inválidos")
    void testUpdate_DadosInvalidos() throws Exception {
        // Given
        User randomUser = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
        HorasExtras horasExtras = createHorasExtras(aprovador, horasExtrasRepository);
        AtualizarHorasExtrasDTO dto = new AtualizarHorasExtrasDTO(
                null,
                null,
                "Nova descricao",
                randomUser.getId()
        );
        String dadosInvalidos = atualizarHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = put(ENDPOINT + "/" + horasExtras.getId()).contentType(APPLICATION_JSON).content(dadosInvalidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad request) quando chamar via PUT o endpoint /horas-extras passando um id invalido")
    void testUpdate_IdInvalido() throws Exception {
        // Given
        User randomUser = createRandomUser(userRepository, UsuarioPerfil.ROLE_GESTOR);
        HorasExtras horasExtras = createHorasExtras(aprovador, horasExtrasRepository);
        AtualizarHorasExtrasDTO dto = new AtualizarHorasExtrasDTO(
                horasExtras.getDataHoraInicio().minusHours(1),
                horasExtras.getDataHoraFim().plusHours(1),
                "Nova descricao",
                randomUser.getId()
        );
        String dadosValidos = atualizarHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = put(ENDPOINT + "/" + 99999).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar 204 (No content) quando chamar via DELETE o endpoint /horas-extras passando um id valido")
    void testDelete() throws Exception {
        // Given
        HorasExtras horasExtras = createHorasExtras(aprovador, horasExtrasRepository);
        RequestBuilder requestBuilder = delete(ENDPOINT + "/" + horasExtras.getId()).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Optional<HorasExtras> optional = horasExtrasRepository.findById(horasExtras.getId());
        assertThat(optional).isPresent();
        assertThat(optional.get().getStatus()).isEqualTo(HorasExtrasStatus.INATIVO);
    }

    @Test
    @DisplayName("Deve retornar 204 (No content) quando chamar via DELETE o endpoint /horas-extras passando um id valido")
    void testDelete_IdInvalido() throws Exception {
        // Given
        HorasExtras horasExtras = createHorasExtras(aprovador, horasExtrasRepository);
        RequestBuilder requestBuilder = delete(ENDPOINT + "/" + 9999).headers(token);

        // When
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        Optional<HorasExtras> optional = horasExtrasRepository.findById(horasExtras.getId());
        assertThat(optional).isPresent();
        assertThat(optional.get().getStatus()).isEqualTo(HorasExtrasStatus.SOLICITADO);
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando aprovar uma hora extra via link")
    void testAvaliarViaLink_Aprovar() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        HorasExtrasDTO horasExtrasDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();

        Link link = linkRepository.findByHorasExtrasIdAndAcao(horasExtrasDTO.id(), AcaoLink.APROVAR).get();
        requestBuilder = get(ENDPOINT + "/avaliar-via-link/" + link.getId());

        // When
        response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 200 (OK) quando recusar uma hora extra via link")
    void testAvaliarViaLink_Recusar() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        HorasExtrasDTO horasExtrasDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();

        Link link = linkRepository.findByHorasExtrasIdAndAcao(horasExtrasDTO.id(), AcaoLink.RECUSAR).get();
        requestBuilder = get(ENDPOINT + "/avaliar-via-link/" + link.getId());

        // When
        response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 404 (Not found) quando tentar avaliar usando um hash invalido")
    void testAvaliarViaLink_HashInvalido() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        HorasExtrasDTO horasExtrasDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();

        Link link = linkRepository.findByHorasExtrasIdAndAcao(horasExtrasDTO.id(), AcaoLink.RECUSAR).get();
        requestBuilder = get(ENDPOINT + "/avaliar-via-link/" + UUID.randomUUID());

        // When
        response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad Request) quando tentar avaliar uma hora extra ja avaliada")
    void testAvaliarViaLink_HoraExtraJaAvaliada() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        HorasExtrasDTO horasExtrasDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();
        HorasExtras horasExtras = horasExtrasRepository.findById(horasExtrasDTO.id()).get();
        horasExtras.setStatus(HorasExtrasStatus.APROVADO);
        horasExtrasRepository.save(horasExtras);

        Link link = linkRepository.findByHorasExtrasIdAndAcao(horasExtrasDTO.id(), AcaoLink.RECUSAR).get();
        requestBuilder = get(ENDPOINT + "/avaliar-via-link/" + link.getId());

        // When
        response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar 404 (Not Found) quando tentar avaliar usando link ja utilizado")
    void testAvaliarViaLink_LinkJaUsado() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        HorasExtrasDTO horasExtrasDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();

        Link link = linkRepository.findByHorasExtrasIdAndAcao(horasExtrasDTO.id(), AcaoLink.RECUSAR).get();
        link.setStatus(LinkStatus.USADO);
        linkRepository.save(link);
        requestBuilder = get(ENDPOINT + "/avaliar-via-link/" + link.getId());

        // When
        response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }
    // tentar avaliar usando link expirado

    @Test
    @DisplayName("Deve retornar 404 (Not Found) quando tentar avaliar usando link expirado")
    void testAvaliarViaLink_LinkExpirado() throws Exception {
        // Given
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        String dadosValidos = novasHorasExtrasDTOJacksonTester.write(dto).getJson();
        RequestBuilder requestBuilder = post(ENDPOINT).contentType(APPLICATION_JSON).content(dadosValidos).headers(token);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        HorasExtrasDTO horasExtrasDTO = horasExtrasDTOJacksonTester.parse(response.getContentAsString()).getObject();

        Link link = linkRepository.findByHorasExtrasIdAndAcao(horasExtrasDTO.id(), AcaoLink.RECUSAR).get();
        link.setStatus(LinkStatus.EXPIRADO);
        linkRepository.save(link);
        requestBuilder = get(ENDPOINT + "/avaliar-via-link/" + link.getId());

        // When
        response = mvc.perform(requestBuilder).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

}