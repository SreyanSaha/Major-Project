package com.help.repository;

import com.help.model.CampaignReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CampaignReportLogRepository extends JpaRepository<CampaignReportLog, Integer> {
    Optional<CampaignReportLog> findByUserIdAndCampaignId(int userId, int campaignId);

    void deleteByCampaignId(int campaignId);
}
