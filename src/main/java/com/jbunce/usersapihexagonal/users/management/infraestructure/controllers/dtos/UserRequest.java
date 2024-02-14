package com.jbunce.usersapihexagonal.users.management.infraestructure.controllers.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastName;

    @Size(min = 10, max = 10)
    private String cellphone;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

}
