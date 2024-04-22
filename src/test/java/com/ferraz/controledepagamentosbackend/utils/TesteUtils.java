package com.ferraz.controledepagamentosbackend.utils;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.infra.security.dto.AuthenticationDTO;
import com.ferraz.controledepagamentosbackend.infra.security.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Service
@ActiveProfiles("test")
public class TesteUtils {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<TokenDTO> tokenDTOJackson;

    @Autowired
    private JacksonTester<AuthenticationDTO> authenticationDTOJackson;

    public static String DEFAULT_PASSWORD = "password";
    public HttpHeaders login(User user) throws Exception {
        String email = user.getEmail();
        String password = DEFAULT_PASSWORD;
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(email, password);
        String credentials = authenticationDTOJackson.write(authenticationDTO).getJson();
        String endpoint = "/login";
        RequestBuilder requestBuilder = post(endpoint).contentType(APPLICATION_JSON).content(credentials);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        String headers = "Bearer " + tokenDTOJackson.parse(response.getContentAsString()).getObject().token();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", headers);
        return httpHeaders;
    }

}
