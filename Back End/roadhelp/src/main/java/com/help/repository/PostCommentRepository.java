package com.help.repository;

import com.help.dto.CommentData;
import com.help.model.PostComment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("SELECT new com.help.dto.CommentData(pc.postCommentId, pc.authorProfileName, u.profileImagePath, pc.commentDateTime, pc.commentDescription, pc.likeCount," +
            " pc.disLikeCount, u.userId, CASE WHEN pc.user.userId = :currentUserId THEN TRUE ELSE FALSE END) FROM PostComment pc JOIN pc.user u WHERE pc.postCommentId = :postCommentId")
    Optional<CommentData> findCommentById(int postCommentId, int currentUserId);

    @Query("SELECT new com.help.dto.CommentData(pc.postCommentId, pc.authorProfileName, u.profileImagePath, pc.commentDateTime, pc.commentDescription, pc.likeCount," +
            " pc.disLikeCount, u.userId, CASE WHEN pc.user.userId = :currentUserId THEN TRUE ELSE FALSE END) FROM PostComment pc JOIN pc.user u WHERE pc.post.postId = :postId " +
            "ORDER BY pc.commentDateTime DESC")
    Page<CommentData> findAllCommentsByPostId(int postId, Pageable pageable, int currentUserId);

}
