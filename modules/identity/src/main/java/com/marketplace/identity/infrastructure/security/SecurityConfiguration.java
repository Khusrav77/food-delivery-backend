package com.marketplace.identity.infrastructure.security;

import com.marketplace.identity.api.exception.ProblemDetailResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/v1/auth/otp/request",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()

                        .anyRequest()
                        .authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setHeader("WWW-Authenticate", "Bearer");
                            ProblemDetailResponses.write(
                                    request,
                                    response,
                                    HttpStatus.UNAUTHORIZED,
                                    "Unauthorized",
                                    "Authentication required");
                        })
                        .accessDeniedHandler((request, response, exception) ->
                                ProblemDetailResponses.write(
                                        request,
                                        response,
                                        HttpStatus.FORBIDDEN,
                                        "Forbidden",
                                        "Access is denied")))

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
