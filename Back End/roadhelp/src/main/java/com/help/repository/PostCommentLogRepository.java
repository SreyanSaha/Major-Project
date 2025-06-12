package com.help.repository;

import com.help.model.PostCommentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PostCommentLogRepository extends JpaRepository<PostCommentLog, Integer> {
    @Query("SELECT pcl FROM PostCommentLog pcl WHERE pcl.userId = :userId AND pcl.postComment.postCommentId = :postCommentId")
    Optional<PostCommentLog> findByUserIdAndPostCommentId(int userId, int postCommentId);
}
