package com.ferraz.controledepagamentosbackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.TokenDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
public class TesteUtils {
    ;

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

}
