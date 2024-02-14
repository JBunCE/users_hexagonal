package com.jbunce.usersapihexagonal.users.management.domain.repositories;

import com.jbunce.usersapihexagonal.users.management.domain.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


public interface IUserMongoDBRepository { // extends MongoRepository<User, String> {
    @Query("{ 'email' : ?0 }")
    User findByEmail(String email);

}
