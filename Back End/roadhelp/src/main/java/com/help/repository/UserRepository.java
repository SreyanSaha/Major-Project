package com.help.repository;

import com.help.model.User;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByAuthData_AuthId(int authId);
    List<User> findByUserFirstNameAndUserLastNameIgnoreCase(String firstName, String lastName);
    List<User> findByUserFirstNameContainingIgnoreCase(String name);
    @Query("SELECT u FROM User u JOIN u.authData a WHERE a.username = :username")
    Optional<User> findByUsername(String username);
}
