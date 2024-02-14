package com.jbunce.usersapihexagonal.users.management.infraestructure.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    private String token;
    private String refreshToken;
}

