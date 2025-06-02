package com.help.repository;

import com.help.dto.PostData;
import com.help.dto.UserPost;
import com.help.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByLatitudeBetweenAndLongitudeBetween(double latMin, double latMax, double lonMin, double lonMax);

    @Query(value = "SELECT * FROM post WHERE LOWER(post_title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(post_description) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY post_upload_date_time DESC", nativeQuery = true)
    Post findPostByPostTitle(@Param("search") String search);

    @Query("SELECT new com.help.dto.PostData(p.postId, p.authorProfileName, u.profileImagePath, p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, " +
            "p.downVoteCount, p.commentCount, p.postReports, p.imagePath1, p.postStatus) FROM Post p JOIN p.user u ORDER BY p.postUploadDateTime DESC LIMIT 10")
    List<PostData> findLimitedPosts();

    @Query("SELECT new com.help.dto.PostData(p.postId, p.authorProfileName, u.profileImagePath, p.postUploadDateTime, p.postTitle, p.postDescription, p.upVoteCount, " +
            "p.downVoteCount, p.commentCount, p.postReports, p.imagePath1, p.postStatus) FROM Post p JOIN p.user u " +
            "WHERE p.postId > :startingId ORDER BY p.postUploadDateTime DESC LIMIT 20")
    List<PostData> findAllPost(@Param("startingId") int startingId);

    @Query("SELECT new com.help.dto.UserPost(p.postId, p.postTitle, p.postDescription, p.imagePath1, p.postStatus) FROM Post p JOIN p.user.authData a WHERE a.username = :username")
    List<UserPost> findAllPostsOfUser(@Param("username")String username);
}
