package com.help.repository;

import com.help.model.UserAuthData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAuthDataRepository extends JpaRepository<UserAuthData,Integer> {
    Optional<UserAuthData> findByUsername(String username);
}
