package com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.filters;


import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.JwtConfig;
import com.mongodb.lang.NonNull;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
public class ApplicationAuthorizationFIlter extends OncePerRequestFilter {

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith(jwtConfig.getTokenPrefix())) {
            String token = bearerToken.replace(jwtConfig.getTokenPrefix(), "");
            UsernamePasswordAuthenticationToken usernamePAT = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(username, null, null);
        } catch (JwtException jwtException) {
            throw new RuntimeException(jwtException.getLocalizedMessage());
        }
    }
}