package com.ferraz.controledepagamentosbackend.utils;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UserStatus;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.TokenDTO;

public class TesteUtils {

    public static String DEFAULT_PASSWORD = "password";

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    
    public static HttpHeaders login(MockMvc mvc, UserRepository userRepository) throws Exception {
        Long id = 1L;
        String email = "teste@teste.com";
        String password = TesteUtils.DEFAULT_PASSWORD;
        String name = "Nome Teste";
        User user = new User(id, name, email, new BCryptPasswordEncoder().encode(password), new BigDecimal("123"), "ROLE_ADMIN", UserStatus.ATIVO, LocalDateTime.now(), null, null, null);
        user = userRepository.save(user);
        return login(mvc, user);
    }
}
