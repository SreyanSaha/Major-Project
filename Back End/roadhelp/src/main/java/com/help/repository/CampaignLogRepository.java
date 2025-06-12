package com.help.repository;

import com.help.model.CampaignLog;
import com.help.model.PostLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampaignLogRepository extends JpaRepository<CampaignLog, Integer> {
    Optional<CampaignLog> findByUserIdAndCampaignId(Integer userId, Integer campaignId);

    void deleteByCampaignId(int campaignId);
}
