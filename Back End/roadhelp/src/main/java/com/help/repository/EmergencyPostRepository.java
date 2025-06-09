package com.help.repository;

import com.help.dto.EmergencyPostData;
import com.help.dto.PostData;
import com.help.model.EmergencyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

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
}
