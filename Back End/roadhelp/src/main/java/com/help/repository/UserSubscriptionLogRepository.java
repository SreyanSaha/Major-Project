package com.help.repository;

import com.help.model.UserSubscriptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSubscriptionLogRepository extends JpaRepository<UserSubscriptionLog, Integer> {
    Optional<UserSubscriptionLog> findByUserId(int userId);
}
