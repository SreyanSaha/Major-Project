package com.help.repository;

import com.help.dto.UserPost;
import com.help.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByLatitudeBetweenAndLongitudeBetween(double latMin, double latMax, double lonMin, double lonMax);
    @Query(value = "SELECT * FROM post WHERE LOWER(post_title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(post_description) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY post_upload_date_time DESC", nativeQuery = true)
    Post findPostByPostTitle(@Param("search") String search);
    @Query(value = "SELECT * FROM post p ORDER BY p.post_upload_date_time DESC LIMIT 10", nativeQuery = true)
    List<Post> findLimitedPosts();
    @Query(value = "SELECT * FROM post p ORDER BY p.post_upload_date_time DESC LIMIT 35", nativeQuery = true)
    List<Post> findAllPost();
    @Query("SELECT new com.help.dto.UserPost(p.postId, p.postTitle, p.postDescription, p.imagePath1, p.postStatus) FROM Post p JOIN p.user.authData a WHERE a.username = :username")
    List<UserPost> findAllPostsOfUser(@Param("username")String username);
}
