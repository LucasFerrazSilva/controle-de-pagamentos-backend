package com.ferraz.controledepagamentosbackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.notificacao.Notificacao;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoService;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalRepository;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.TokenDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import static com.ferraz.controledepagamentosbackend.domain.user.UserStatus.ATIVO;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TesteUtils {

    public static String DEFAULT_PASSWORD = "password";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static User defaultUser;


    public static HttpHeaders login(MockMvc mvc, UserRepository userRepository) throws Exception {
        if (defaultUser == null) {
            Long id = 1L;
            String email = "teste@teste.com";
            String password = TesteUtils.DEFAULT_PASSWORD;
            String name = "Nome Teste";
            defaultUser = new User(id, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), UsuarioPerfil.ROLE_ADMIN, ATIVO, LocalDateTime.now(), null, null, null);
            userRepository.save(defaultUser);
        }

        return login(mvc, defaultUser);
    }

    public static User createAprovador(UserRepository userRepository) {
        String email = "aprovador@mail.com";
        String password = TesteUtils.DEFAULT_PASSWORD;
        String name = "Nome Aprovador";
        User user = new User(null, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), UsuarioPerfil.ROLE_GESTOR, ATIVO, LocalDateTime.now(), null, null, null);
        return userRepository.save(user);
    }

    @Transactional
    public static User createRandomUser(UserRepository userRepository, UsuarioPerfil perfil) {
        int randomNumber = new Random().nextInt(1000000);
        DadosCreateUserDTO dto = new DadosCreateUserDTO("Usuario " + randomNumber, randomNumber + "@mail.com", new BCryptPasswordEncoder().encode(DEFAULT_PASSWORD), new BigDecimal(randomNumber), perfil);
        User user = new User(dto);
        return userRepository.save(user);
    }

    public static HttpHeaders login(MockMvc mvc, User user) throws Exception {
        String email = user.getEmail();
        String password = DEFAULT_PASSWORD;
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(email, password);
        String credentials = objectMapper.writeValueAsString(authenticationDTO);
        String endpoint = "/login";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(credentials);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        String headers = "Bearer " + objectMapper.readValue(response.getContentAsString(), TokenDTO.class).token();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", headers);
        return httpHeaders;
    }

    @Transactional
    public static NotaFiscal createNotaFiscal(User user, NotaFiscalRepository repository){
        NovaNotaFiscalDTO dto = new NovaNotaFiscalDTO(
                user.getId(),
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getYear(),
                BigDecimal.valueOf(2000),
                "TEST"
        );
        NotaFiscal notaFiscal = new NotaFiscal(dto, user);
        return repository.save(notaFiscal);
    }

    @Transactional
    public static HorasExtras createHorasExtras(User user, User aprovador, HorasExtrasRepository repository) {
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        HorasExtras horasExtras = new HorasExtras(dto, user, aprovador);
        return repository.save(horasExtras);
    }

    public static Notificacao criarNotificacao(User user, NotificacaoService notificacaoService) {
        String descricao = "Aprovador X aprovou a sua hora extra do dia 20/05/2024";
        String path = "/horas-extras";
        return notificacaoService.create(user, descricao, path);
    }

}
