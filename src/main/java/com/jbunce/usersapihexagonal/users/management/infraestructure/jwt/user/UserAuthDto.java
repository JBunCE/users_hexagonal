package com.jbunce.usersapihexagonal.users.management.infraestructure.jwt.user;

import lombok.Data;

@Data
public class UserAuthDto {
    private String email;
    private String password;
}

