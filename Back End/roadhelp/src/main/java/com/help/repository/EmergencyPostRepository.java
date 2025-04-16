package com.help.repository;

import com.help.model.EmergencyPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyPostRepository extends JpaRepository<EmergencyPost, Integer> {
}
