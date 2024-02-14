package com.jbunce.usersapihexagonal.users.management.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Embeddable
@Getter @Setter
public class Credential {

    @Column(unique = true)
    @Indexed(unique = true)
    private String email;
    private String password;

}
