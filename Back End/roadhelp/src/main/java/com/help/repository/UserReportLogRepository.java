package com.help.repository;

import com.help.model.UserReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserReportLogRepository extends JpaRepository<UserReportLog, Integer> {

    @Query("SELECT u FROM UserReportLog u WHERE u.reporterUserId = :reporterUserId AND u.reportedUserId = :reportedUserId")
    Optional<UserReportLog> findByUserIds(@Param("reporterUserId") int reporterUserId, @Param("reportedUserId") int reportedUserId);
}
