package com.help.repository;

import com.help.dto.CommentData;
import com.help.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("SELECT new com.help.dto.CommentData(pc.postCommentId, pc.authorProfileName, u.profileImagePath, pc.commentDateTime, pc.commentDescription, pc.likeCount," +
            " pc.disLikeCount, u.userId) FROM PostComment pc JOIN pc.user u WHERE pc.postCommentId = :postCommentId")
    Optional<CommentData> findCommentById(int postCommentId);

    @Query("SELECT new com.help.dto.CommentData(pc.postCommentId, pc.authorProfileName, u.profileImagePath, pc.commentDateTime, pc.commentDescription, pc.likeCount," +
            " pc.disLikeCount, u.userId) FROM PostComment pc JOIN pc.user u WHERE pc.post.postId = :postId")
    List<CommentData> findAllCommentsByPostId(int postId);
}
