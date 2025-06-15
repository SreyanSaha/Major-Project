package com.help.repository;

import com.help.dto.FullPostData;
import com.help.dto.PostData;
import com.help.dto.ServiceResponse;
import com.help.dto.UserPost;
import com.help.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT new com.help.dto.PostData(p.postId, u.civicTrustScore, CONCAT(u.userFirstName, ' ', u.userLastName), u.profileImagePath, p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, " +
            "p.downVoteCount, p.commentCount, p.postReports, p.imagePath1, p.postStatus) FROM Post p JOIN p.user u ORDER BY p.postUploadDateTime DESC")
    List<PostData> findLimitedPosts(Pageable pageable);

    @Query("SELECT new com.help.dto.PostData(p.postId, u.civicTrustScore, CONCAT(u.userFirstName, ' ', u.userLastName), u.profileImagePath, p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, " +
            "p.downVoteCount, p.commentCount, p.postReports, p.imagePath1, p.postStatus) FROM Post p JOIN p.user u ORDER BY p.postUploadDateTime DESC")
    Page<PostData> findAllByOrderByPostUploadDateTimeDesc(Pageable pageable);

    @Query("SELECT new com.help.dto.UserPost(p.postId, p.postTitle, p.postDescription, p.imagePath1, p.postStatus) FROM Post p JOIN p.user.authData a WHERE a.username = :username")
    List<UserPost> findAllPostsOfUser(@Param("username")String username);

    @Query("SELECT new com.help.dto.FullPostData(p.id, CONCAT(u.userFirstName, ' ', u.userLastName), p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, p.downVoteCount, p.commentCount, " +
            "p.imagePath1, p.imagePath2, p.imagePath3, p.imagePath4, p.imagePath5, p.afterWorkImagePath1, p.afterWorkImagePath2, p.afterWorkImagePath3, p.afterWorkImagePath4, p.afterWorkImagePath5, " +
            "p.latitude, p.longitude, p.street, p.city, p.state, p.country, p.postalCode, p.postReports, p.postStatus, u.userId, u.profileImagePath, u.civicTrustScore) FROM Post p JOIN p.user u WHERE p.id = :postId")
    Optional<FullPostData> findFullPostById(@Param("postId") int postId);

    @Query("SELECT new com.help.dto.PostData(p.postId, u.civicTrustScore, CONCAT(u.userFirstName, ' ', u.userLastName), " +
            "u.profileImagePath, p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, " +
            "p.downVoteCount, p.commentCount, p.postReports, p.imagePath1, p.postStatus) " +
            "FROM Post p JOIN p.user u " +
            "WHERE LOWER(p.postTitle) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(p.postDescription) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(p.street) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(p.city) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(p.state) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(p.postalCode) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    Page<PostData> findAllBySearchString(@Param("searchString") String searchString, Pageable pageable);

    @Query("SELECT new com.help.dto.FullPostData(p.id, CONCAT(u.userFirstName, ' ', u.userLastName), p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, p.downVoteCount, p.commentCount, " +
            "p.imagePath1, p.imagePath2, p.imagePath3, p.imagePath4, p.imagePath5, p.afterWorkImagePath1, p.afterWorkImagePath2, p.afterWorkImagePath3, p.afterWorkImagePath4, p.afterWorkImagePath5, " +
            "p.latitude, p.longitude, p.street, p.city, p.state, p.country, p.postalCode, p.postReports, p.postStatus, u.userId, u.profileImagePath, u.civicTrustScore) " +
            "FROM Post p JOIN p.user u WHERE p.postStatus < 1")
    List<FullPostData> findAllNonCompletedPosts();

    @Query("SELECT new com.help.dto.FullPostData(p.id, CONCAT(u.userFirstName, ' ', u.userLastName), p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, p.downVoteCount, p.commentCount, " +
            "p.imagePath1, p.imagePath2, p.imagePath3, p.imagePath4, p.imagePath5, p.afterWorkImagePath1, p.afterWorkImagePath2, p.afterWorkImagePath3, p.afterWorkImagePath4, p.afterWorkImagePath5, " +
            "p.latitude, p.longitude, p.street, p.city, p.state, p.country, p.postalCode, p.postReports, p.postStatus, u.userId, u.profileImagePath, u.civicTrustScore) " +
            "FROM Post p JOIN p.user u " +
            "WHERE p.postStatus < 1 AND (" +
            "LOWER(p.postTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.postDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.street) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.state) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.country) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            ")")
    List<FullPostData> searchPosts(@Param("keyword") String keyword);


}
