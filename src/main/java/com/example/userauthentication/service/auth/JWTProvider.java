package com.example.userauthentication.service.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JWTProvider {
    private static final String AGGREGATE_ID_CLAIM = "aggregate_id";
    private static final String USERNAME_CLAIM = "username";
    private static final String DESCRIPTION_CLAIM = "description";
    private static final String STATUS_CLAIM = "status";
    private static final String ROLES_CLAIM = "roles";

    @Value("${api.security.token.expiration-in-hours}")
    private Long expirationInHours;

    @Autowired
    private JwtEncoder jwtEncoder;

    public String createToken(CreateTokenDTO tokenCreation) {


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(tokenCreation.username())
                .claim(AGGREGATE_ID_CLAIM, tokenCreation.aggregateId())
                .claim(USERNAME_CLAIM, tokenCreation.username())
                .claim(DESCRIPTION_CLAIM, tokenCreation.description())
                .claim(STATUS_CLAIM, tokenCreation.status())
                .claim(ROLES_CLAIM, StringUtils.join(tokenCreation.roles(), ","))
                .expiresAt(genExpirationDate())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(expirationInHours).atZone(ZoneId.systemDefault()).toInstant();
    }
}
