package com.jbunce.usersapihexagonal.users.management.infraestructure.controllers;

import com.jbunce.usersapihexagonal.configurations.BaseResponse;
import com.jbunce.usersapihexagonal.users.management.application.IUserService;
import com.jbunce.usersapihexagonal.users.management.infraestructure.controllers.dtos.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/reg")
    public ResponseEntity<BaseResponse> register(@RequestBody UserRequest request) {
        return userService.registerUser(request).apply();
    }

    @GetMapping("{token}/activate/{userId}")
    public ResponseEntity<BaseResponse> activateUser(@PathVariable String token,
                                                     @PathVariable String userId) {
        return userService.activeUser(token, userId).apply();
    }

}
