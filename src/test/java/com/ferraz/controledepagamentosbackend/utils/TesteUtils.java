package com.ferraz.controledepagamentosbackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtras;
import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasRepository;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.HorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;
import com.ferraz.controledepagamentosbackend.domain.user.dto.DadosCreateUserDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.TokenDTO;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import static com.ferraz.controledepagamentosbackend.domain.user.UserStatus.ATIVO;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TesteUtils {

    public static String DEFAULT_PASSWORD = "password";

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static HttpHeaders login(MockMvc mvc, UserRepository userRepository) throws Exception {
        Long id = 1L;
        String email = "teste@teste.com";
        String password = TesteUtils.DEFAULT_PASSWORD;
        String name = "Nome Teste";
        User user = new User(id, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), "ROLE_ADMIN", ATIVO, LocalDateTime.now(), null, null, null);
        user = userRepository.save(user);
        return login(mvc, user);
    }

    public static User createAprovador(UserRepository userRepository) {
        String email = "aprovador@mail.com";
        String password = TesteUtils.DEFAULT_PASSWORD;
        String name = "Nome Aprovador";
        User user = new User(null, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), "ROLE_ADMIN", ATIVO, LocalDateTime.now(), null, null, null);
        return userRepository.save(user);
    }

    public static User createRandomUser(UserRepository userRepository) {
        int randomNumber = new Random().nextInt(1000000);
        DadosCreateUserDTO dto = new DadosCreateUserDTO("Usuario " + randomNumber, randomNumber + "@mail.com", DEFAULT_PASSWORD, new BigDecimal(randomNumber), "ROLE_ADMIN");
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

    
    public static User createUser(UserRepository userRepository) {
    	User user = new User();
    	user.setNome("Luis");
    	user.setEmail("test@test.com.br");
    	user.setSenha(new BCryptPasswordEncoder().encode("1234"));
    	user.setSalario(new BigDecimal("100.0"));
    	user.setPerfil("ROLE_ADMIN");
		user.setStatus(UserStatus.ATIVO);
		user.setCreateDateTime(LocalDateTime.now());
		user.setUpdateDatetime(null);
		user.setUpdateUser(null);
    	userRepository.save(user);
    	return user;
    }

    public static HorasExtras createHorasExtras(User aprovador, HorasExtrasRepository repository) throws Exception {
        NovasHorasExtrasDTO dto = new NovasHorasExtrasDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "Descricao hora extra",
                aprovador.getId());
        HorasExtras horasExtras = new HorasExtras(dto, aprovador, aprovador);
        return repository.save(horasExtras);
    }

}
