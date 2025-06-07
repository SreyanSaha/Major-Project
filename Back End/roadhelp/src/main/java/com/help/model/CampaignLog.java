package com.help.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CampaignLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int campaignLogId;
    private int userId, campaignId;
    private short log;//1 -> like, 0 -> disLike

    public CampaignLog() {}

    public CampaignLog(int userId, int campaignId, short log) {
        this.userId = userId;
        this.campaignId = campaignId;
        this.log = log;
    }

    public int getCampaignLogId() {
        return campaignLogId;
    }

    public void setCampaignLogId(int campaignLogId) {
        this.campaignLogId = campaignLogId;
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
