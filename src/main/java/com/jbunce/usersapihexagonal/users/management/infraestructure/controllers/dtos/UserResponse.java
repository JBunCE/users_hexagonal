package com.jbunce.usersapihexagonal.users.management.infraestructure.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {

    private String id;
    private String name;
    private String lastName;
    private String cellphone;
    private String email;
    private String verifiedAt;

}
