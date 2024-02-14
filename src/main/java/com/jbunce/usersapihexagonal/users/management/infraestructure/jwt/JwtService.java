package com.jbunce.usersapihexagonal.users.management.infraestructure.jwt;

import com.jbunce.usersapihexagonal.configurations.BaseResponse;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.user.UserDetailsImpl;
import com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.user.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.LocalDate;

@Service
public class JwtService {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private UserDetailsServiceImpl UserDetailsService;

    public BaseResponse refresh(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        String email = claims.getSubject();
        UserDetailsImpl userDetails = (UserDetailsImpl) UserDetailsService.loadUserByUsername(email);

        Date expirationDate = Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpiration()));
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new java.util.Date())
                .setExpiration(expirationDate)
                .signWith(secretKey).compact();

        Date refreshExpirationDate = Date.valueOf(LocalDate.now().plusDays(jwtConfig.getRefreshTokenExpiration()));
        String newRefreshToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new java.util.Date())
                .setExpiration(refreshExpirationDate)
                .signWith(secretKey).compact();

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(token);
        jwtResponse.setRefreshToken(newRefreshToken);

        return BaseResponse.builder()
                .data(jwtResponse)
                .message("Successfully authenticated")
                .success(Boolean.TRUE)
                .status(HttpStatus.OK)
                .code(200).build();
    }

}
