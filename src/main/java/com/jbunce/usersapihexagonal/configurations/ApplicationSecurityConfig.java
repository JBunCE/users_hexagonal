package com.jbunce.usersapihexagonal.configurations;

import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.JwtConfig;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.filters.ApplicationAuthenticationFIlter;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.filters.ApplicationAuthorizationFIlter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private ApplicationAuthorizationFIlter authorizationFilter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private SecretKey secretKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        ApplicationAuthenticationFIlter authenticationFilter = new ApplicationAuthenticationFIlter(jwtConfig, secretKey);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/users/login");

        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
        );
        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, ApplicationAuthenticationFIlter.class);
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/default/health").permitAll();
            auth.requestMatchers("/token/refresh").permitAll();
            auth.requestMatchers("/users/reg").permitAll();
            auth.requestMatchers("/users/{token}/activate/{userId}").permitAll();
        });

        return http.httpBasic(Customizer.withDefaults()).build();
    }
}