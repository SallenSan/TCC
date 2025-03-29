package com.tcc.consultas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desativa proteção CSRF para testes no Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/pacientes/**").permitAll()
                        .requestMatchers("/consultas/**").permitAll()
                        .requestMatchers("/psicologos/**").permitAll()// Permite acesso sem autenticação
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

