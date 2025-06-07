package com.help.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CampaignReportLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int campaignReportLogId;
    private int userId, campaignId;
    private short log;//1 -> reported

    public CampaignReportLog() {}

    public CampaignReportLog(int userId, int campaignId, short log) {
        this.userId = userId;
        this.campaignId = campaignId;
        this.log = log;
    }

    public int getCampaignReportLogId() {
        return campaignReportLogId;
    }

    public void setCampaignReportLogId(int campaignReportLogId) {
        this.campaignReportLogId = campaignReportLogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
