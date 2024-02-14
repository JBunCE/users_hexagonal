package com.jbunce.usersapihexagonal.users.management.domain.repositories;

import com.jbunce.usersapihexagonal.users.management.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserMySQLRepository extends JpaRepository<User, String> {

    @Query(value = """
        SELECT u.* FROM `users` u WHERE u.email = :email
    """, nativeQuery = true)
    Optional<User> getUserByEmail(String email);

}
