package com.ferraz.controledepagamentosbackend.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        		.cors(withDefaults())
        	    .csrf(CsrfConfigurer::disable)
        	    .sessionManagement(sessionManagement ->
        	        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	    )
        	    .authorizeHttpRequests(authorizeHttpRequests ->
        	        authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/horas-extras/avaliar-via-link/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/prometheus").permitAll()
        	            .anyRequest().authenticated()
        	    )
        	    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        	    .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}