package com.ferraz.controledepagamentosbackend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") // Busca a propriedade no application.properties
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getEmail()) // Identifica o usuário
                .withClaim("id", user.getId()) // Podemos adicionar qualquer informação relevante usando o withClaim
                .withClaim("name", user.getNome())
                .withExpiresAt(generateExpirationDate()) // É importante definir um tempo de expiração do token
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractSubject(String token) {
        var algorithm = Algorithm.HMAC256(secret);
        var verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        var decodedJWT = verifier.verify(token); // Valida o token

        return decodedJWT.getSubject(); // Pega o subject
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}