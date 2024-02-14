package com.jbunce.usersapihexagonal.users.management.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
//@Document
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Embedded
    private Contact contact;

    @Embedded
    private Credential credential;

    @Embedded
    private Status status;

}
