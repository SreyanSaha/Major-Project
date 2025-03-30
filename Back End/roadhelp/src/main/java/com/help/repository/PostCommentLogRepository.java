package com.help.repository;

import com.help.model.PostCommentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentLogRepository extends JpaRepository<PostCommentLog, Integer> {
}
