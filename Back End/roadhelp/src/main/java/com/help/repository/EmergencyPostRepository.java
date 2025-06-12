package com.help.repository;

import com.help.dto.EmergencyPostData;
import com.help.dto.FullEmergencyPostData;
import com.help.model.EmergencyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EmergencyPostRepository extends JpaRepository<EmergencyPost, Integer> {
    @Query(value = "SELECT * FROM emergency_post e ORDER BY e.emergency_post_upload_date_time DESC LIMIT 35", nativeQuery = true)
    List<EmergencyPost> findAllEmergencyPostForHome();

    @Query(value = "SELECT * FROM emergency_post WHERE LOWER(emergency_post_title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(emergency_post_description) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY emergency_post_upload_date_time DESC", nativeQuery = true)
    List<EmergencyPost> searchAllEmergencyPost(@Param("search") String search);

    @Query("SELECT new com.help.dto.EmergencyPostData(e.emergencyPostId, e.imagePath1, e.emergencyPostTitle, e.emergencyPostDescription, CONCAT(u.userFirstName, ' ', u.userLastName), " +
            "u.profileImagePath, e.emergencyPostUploadDateTime, e.emergencyPostStatus, u.civicTrustScore, e.street, e.city, e.state, e.country, e.zipCode) FROM EmergencyPost e JOIN e.user u " +
            "ORDER BY e.emergencyPostUploadDateTime DESC")
    List<EmergencyPostData> findLimitedEmergencyPost(Pageable pageable);

    @Query("SELECT new com.help.dto.EmergencyPostData(e.emergencyPostId, e.imagePath1, e.emergencyPostTitle, e.emergencyPostDescription, CONCAT(u.userFirstName, ' ', u.userLastName), " +
            "u.profileImagePath, e.emergencyPostUploadDateTime, e.emergencyPostStatus, u.civicTrustScore, e.street, e.city, e.state, e.country, e.zipCode) FROM EmergencyPost e JOIN e.user u " +
            "ORDER BY e.emergencyPostUploadDateTime DESC")
    Page<EmergencyPostData> findAllByOrderByEmergencyPostUploadDateTimeDesc(PageRequest pageRequest);

    @Query("SELECT new com.help.dto.EmergencyPostData(e.emergencyPostId, e.imagePath1, e.emergencyPostTitle, e.emergencyPostDescription, e.emergencyPostStatus) " +
            "FROM EmergencyPost e JOIN e.user.authData a WHERE a.username = :username")
    List<EmergencyPostData> findAllPostsOfUser(@Param("username") String username);

    @Query("SELECT new com.help.dto.EmergencyPostData(e.emergencyPostId, e.imagePath1, e.emergencyPostTitle, e.emergencyPostDescription, e.emergencyPostStatus) " +
            "FROM EmergencyPost e WHERE e.emergencyPostId= :emergencyPostId")
    Optional<EmergencyPostData> findEmergencyPostById(@Param("emergencyPostId") int emergencyPostId);

    @Query("""
    SELECT new com.help.dto.FullEmergencyPostData(ep.emergencyPostId,CONCAT(u.userFirstName, ' ', u.userLastName),u.profileImagePath,u.userId,u.civicTrustScore,
    ep.imagePath1,ep.imagePath2,ep.imagePath3,ep.imagePath4,ep.imagePath5,ep.audioFilePath,ep.emergencyPostTitle,ep.emergencyPostDescription,
    ep.emergencyPostUploadDateTime,ep.latitude,ep.longitude,ep.street,ep.city, ep.state,ep.country,ep.zipCode,ep.emergencyPostStatus)
    FROM EmergencyPost ep JOIN ep.user u WHERE ep.id = :postId
    """)
    Optional<FullEmergencyPostData> getFullEmergencyPostDataById(@Param("postId") int postId);

    @Query("SELECT new com.help.dto.EmergencyPostData(e.emergencyPostId, e.imagePath1, e.emergencyPostTitle, e.emergencyPostDescription, " +
            "CONCAT(u.userFirstName, ' ', u.userLastName), u.profileImagePath, e.emergencyPostUploadDateTime, e.emergencyPostStatus, " +
            "u.civicTrustScore, e.street, e.city, e.state, e.country, e.zipCode) " +
            "FROM EmergencyPost e JOIN e.user u " +
            "WHERE LOWER(e.emergencyPostTitle) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(e.emergencyPostDescription) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(e.street) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(e.city) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(e.state) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(e.country) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(e.zipCode) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    Page<EmergencyPostData> findAllBySearchString(@Param("searchString") String searchString, Pageable pageable);

}
