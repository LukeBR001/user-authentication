package com.example.userauthentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    SecurityFilter securityFilter;

    // private final RSAPublicKey publicKey;
    // private final RSAPrivateKey  privateKey;

    private final CustomBasicAuthEntryPoint customBasicAuthEntryPoint;


    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    public SecurityConfig(CustomBasicAuthEntryPoint customBasicAuthEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAuthenticationEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler) {
        this.customBasicAuthEntryPoint = customBasicAuthEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.
                        accessDeniedHandler(customBearerTokenAccessDeniedHandler)
                        .authenticationEntryPoint(customBasicAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
