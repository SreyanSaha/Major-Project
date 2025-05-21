package com.help.repository;

import com.help.dto.UserCampaign;
import com.help.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    @Query("SELECT new com.help.dto.UserCampaign(c.campaignId, c.campaignTitle, c.campaignDescription, c.status, c.imagePath1) FROM Campaign c JOIN c.user.authData a WHERE a.username = :username")
    List<UserCampaign> findAllCampaignsOfUser(@Param("username") String username);
}
