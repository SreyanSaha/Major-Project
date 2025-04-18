package com.help.repository;

import com.help.model.EmergencyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmergencyPostRepository extends JpaRepository<EmergencyPost, Integer> {
    @Query(value = "SELECT * FROM emergency_post e ORDER BY e.emergency_post_upload_date_time DESC LIMIT 10", nativeQuery = true)
    List<EmergencyPost> findAllEmergencyPostForHome();
}
