package com.jbunce.usersapihexagonal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class UsersApiHexagonalApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApiHexagonalApplication.class, args);
	}

}
