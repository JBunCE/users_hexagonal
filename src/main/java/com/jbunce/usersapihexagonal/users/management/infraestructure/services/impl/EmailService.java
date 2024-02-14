package com.jbunce.usersapihexagonal.users.management.infraestructure.services.impl;

import com.jbunce.usersapihexagonal.users.management.infraestructure.services.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendVerificationEmail(String email, String token, String userId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(email);
        message.setSubject("Verification Email");
        message.setText("Complete Registration at: http://localhost:8080/api/v1/users/" + token + "/activate/" + userId);
        emailSender.send(message);
    }
}
