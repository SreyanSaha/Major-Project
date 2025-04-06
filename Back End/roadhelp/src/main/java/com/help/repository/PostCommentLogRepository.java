package com.help.repository;

import com.help.model.PostCommentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PostCommentLogRepository extends JpaRepository<PostCommentLog, Integer> {
    Optional<PostCommentLog> findByUserIdAndPostCommentId(int userId, int postCommentId);
}
