package com.example.userauthentication.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class JWTProvider {

    @Value("${api.security.token.expiration-in-hours}")
    private Long expirationInHours;

    @Autowired
    private JwtEncoder jwtEncoder;

    public String createToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .expiresAt(genExpirationDate())
                .subject(authentication.getName())
                .claim("roles", StringUtils.join(roles, ","))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(expirationInHours).atZone(ZoneId.systemDefault()).toInstant();
    }
}
