package com.jbunce.usersapihexagonal.users.management.application;

import com.jbunce.usersapihexagonal.configurations.BaseResponse;
import com.jbunce.usersapihexagonal.users.management.domain.entities.User;
import com.jbunce.usersapihexagonal.users.management.infraestructure.controllers.dtos.UserRequest;

public interface IUserService {

    BaseResponse registerUser(UserRequest request);

    BaseResponse activeUser(String token, String userId);

    User findByEmail(String email);

}
