package com.example.userauthentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.userauthentication.exception.auth.JWTAuthenticationException;
import com.example.userauthentication.exception.auth.JWTGenerateTokenException;
import com.example.userauthentication.repository.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(UserEntity user) {
        try {
            return JWT.create()
                    .withIssuer("user-authentication")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .sign(getAlgorithm());
        } catch (JWTCreationException ex) {
            throw new JWTGenerateTokenException("Error while generate token", ex);
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(getAlgorithm())
                    .withIssuer("user-authentication")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
                throw new JWTAuthenticationException("Invalid token: ", ex);
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}