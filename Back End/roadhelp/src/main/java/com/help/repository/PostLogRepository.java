package com.help.repository;

import com.help.model.PostLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLogRepository extends JpaRepository<PostLog, Integer> {
    Optional<PostLog> findByUserIdAndPostId(Integer userId, Integer postId);
}
