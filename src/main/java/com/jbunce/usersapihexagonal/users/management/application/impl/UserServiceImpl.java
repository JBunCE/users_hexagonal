package com.jbunce.usersapihexagonal.users.management.application.impl;

import com.jbunce.usersapihexagonal.configurations.BaseResponse;
import com.jbunce.usersapihexagonal.users.management.application.IUserService;
import com.jbunce.usersapihexagonal.users.management.domain.entities.Contact;
import com.jbunce.usersapihexagonal.users.management.domain.entities.Credential;
import com.jbunce.usersapihexagonal.users.management.domain.entities.Status;
import com.jbunce.usersapihexagonal.users.management.domain.entities.User;
import com.jbunce.usersapihexagonal.users.management.domain.repositories.IUserMySQLRepository;
import com.jbunce.usersapihexagonal.users.management.infraestructure.controllers.dtos.UserRequest;
import com.jbunce.usersapihexagonal.users.management.infraestructure.controllers.dtos.UserResponse;
import com.jbunce.usersapihexagonal.users.management.infraestructure.services.IEmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMySQLRepository userRepository;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public BaseResponse registerUser(UserRequest request) {
        User savedUser = userRepository.save(toUser(request));

        emailService.sendVerificationEmail(
                savedUser.getCredential().getEmail(),
                savedUser.getStatus().getToken(),
                savedUser.getId()
        );

        return BaseResponse.builder()
                .data(toUserResponse(savedUser))
                .message("The user was saved with id: " + savedUser.getId())
                .success(Boolean.TRUE)
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value()).build();
    }

    @Override
    public BaseResponse activeUser(String token, String stringId) {
        User user = userRepository.findById(stringId).orElseThrow(EntityNotFoundException::new);

        if (user.getStatus().getToken().equals(token)) {
            user.getStatus().setVerifiedAt(LocalDate.now());
            userRepository.save(user);

            return BaseResponse.builder()
                    .data(toUserResponse(user))
                    .message("The user was activated with id: " + user.getId())
                    .success(Boolean.TRUE)
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value()).build();
        }

        throw new RuntimeException("The token is not valid");
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    private User toUser(UserRequest request) {
        User user = new User();

        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setLastName(request.getLastName());
        contact.setCellphone(request.getCellphone());

        Credential credential = new Credential();
        credential.setEmail(request.getEmail());
        credential.setPassword(passwordEncoder.encode(request.getPassword()));

        Status status = new Status();
        status.setToken(UUID.randomUUID().toString());

        user.setContact(contact);
        user.setCredential(credential);
        user.setStatus(status);

        return user;
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getContact().getName());
        response.setLastName(user.getContact().getLastName());
        response.setCellphone(user.getContact().getCellphone());
        response.setEmail(user.getCredential().getEmail());

        return response;
    }

}
