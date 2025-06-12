package com.help.repository;

import com.help.model.PostReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface PostReportLogRepository extends JpaRepository<PostReportLog, Integer> {
    Optional<PostReportLog> findByUserIdAndPostId(int userId, int postId);

    void deleteByPostId(@Param("postId") int postId);
}
