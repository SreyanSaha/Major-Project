package com.help.repository;

import com.help.model.EmergencyPost;
import com.help.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByLatitudeBetweenAndLongitudeBetween(double latMin, double latMax, double lonMin, double lonMax);
    Post findPostByPostTitle(String searchTitle);
    @Query(value = "SELECT * FROM post p ORDER BY p.post_upload_date_time DESC LIMIT 10", nativeQuery = true)
    List<EmergencyPost> findAllPostForHome();
}
