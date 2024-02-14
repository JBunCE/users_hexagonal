package com.jbunce.usersapihexagonal.users.management.infraestructure.services;

public interface IEmailService {

    void sendVerificationEmail(String email, String token, String userId);

}
