package com.help.repository;

import com.help.model.UserAuthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAuthDataRepository extends JpaRepository<UserAuthData,Integer> {
    Optional<UserAuthData> findByUsername(String username);
}
