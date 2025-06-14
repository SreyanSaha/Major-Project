package com.help.repository;

import com.help.dto.ServiceResponse;
import com.help.dto.UserProfile;
import com.help.model.User;
import com.help.model.UserAuthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAuthDataRepository extends JpaRepository<UserAuthData,Integer> {
    Optional<UserAuthData> findByUsername(String username);
    void deleteByUsername(String username);
    Optional<UserAuthData> findByUser_UserId(int userId);

}
