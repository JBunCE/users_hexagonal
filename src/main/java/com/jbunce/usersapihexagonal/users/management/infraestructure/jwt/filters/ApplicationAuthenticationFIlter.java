package com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbunce.usersapihexagonal.configurations.BaseResponse;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.JwtConfig;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.JwtResponse;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.user.UserAuthDto;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.user.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
public class ApplicationAuthenticationFIlter extends UsernamePasswordAuthenticationFilter {

    private JwtConfig jwtConfig;

    private SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        UserAuthDto authDto;

        try {
            authDto = new ObjectMapper().readValue(request.getReader(), UserAuthDto.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getLocalizedMessage());
        }

        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                authDto.getEmail(),
                authDto.getPassword()
        );

        return getAuthenticationManager().authenticate(usernamePAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl applicationUserDetails = (UserDetailsImpl) authResult.getPrincipal();

        Date expirationDate = Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpiration()));
        String token = Jwts.builder()
                .setSubject(applicationUserDetails.getUsername())
                .claim("authorities", applicationUserDetails.getAuthorities())
                .setIssuedAt(new java.util.Date())
                .setExpiration(expirationDate)
                .signWith(secretKey).compact();

        Date refreshExpirationDate = Date.valueOf(LocalDate.now().plusDays(jwtConfig.getRefreshTokenExpiration()));
        String refreshToken = Jwts.builder()
                .setSubject(applicationUserDetails.getUsername())
                .setIssuedAt(new java.util.Date())
                .setExpiration(refreshExpirationDate)
                .signWith(secretKey).compact();

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(token);
        jwtResponse.setRefreshToken(refreshToken);

        response.addHeader("Authorization", jwtConfig.getTokenPrefix() + token);

        BaseResponse baseResponse = BaseResponse.builder()
                .data(jwtResponse)
                .message("Successfully authenticated")
                .success(Boolean.TRUE)
                .status(HttpStatus.OK)
                .code(200)
                .build();
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(baseResponse));
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
