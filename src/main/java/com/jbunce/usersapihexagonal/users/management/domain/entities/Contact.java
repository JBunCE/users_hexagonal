package com.jbunce.usersapihexagonal.users.management.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Embeddable
@Getter @Setter
public class Contact {

    private String name;
    private String lastName;
    private String cellphone;

    public String getFullName() {
        return name + " " + lastName;
    }

}
