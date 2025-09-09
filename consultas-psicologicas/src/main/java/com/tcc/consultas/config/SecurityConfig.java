package com.tcc.consultas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // públicos (ajuste conforme sua necessidade)
                        .requestMatchers("/pacientes/**").permitAll()
                        .requestMatchers("/psicologos/**").permitAll()
                        .requestMatchers("/consultas/**").permitAll()
                        .requestMatchers("/teste-email").permitAll()
                        .requestMatchers("/usuario-admin").permitAll()
                        // troca de senha requer usuário autenticado
                        .requestMatchers("/auth/change-password").authenticated()
                        // o restante, por ora, liberado
                        .anyRequest().permitAll()
                )
                // HTTP Basic para autenticação da troca de senha
                .httpBasic(withDefaults());

        return http.build();
    }

    // CORS — importante permitir Authorization
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://127.0.0.1:5500",
                "http://127.0.0.1:5501",
                "http://localhost:5500",
                "http://localhost:5501"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
