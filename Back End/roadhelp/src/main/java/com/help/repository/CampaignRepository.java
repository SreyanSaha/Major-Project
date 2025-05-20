package com.help.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.help.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    @Query(value = "SELECT c.* FROM campaign c JOIN user_auth_data a ON c.user_id=a.user_id WHERE a.username=:username", nativeQuery = true)
    List<Campaign> findAllCampaignsOfUser(@Param("username") String username);
}
