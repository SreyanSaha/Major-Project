package com.help.repository;

import com.help.model.UserSubscriptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSubscriptionLogRepository extends JpaRepository<UserSubscriptionLog, Integer> {

    Optional<UserSubscriptionLog> findByUserId(@Param("userId") int userId);
    void deleteByUserId(@Param("userId") int userId);
}
