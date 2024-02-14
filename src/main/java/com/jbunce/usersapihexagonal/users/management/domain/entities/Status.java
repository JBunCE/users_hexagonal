package com.jbunce.usersapihexagonal.users.management.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Embeddable
@Getter @Setter
public class Status {

    private String token;
    private LocalDate verifiedAt;

}
