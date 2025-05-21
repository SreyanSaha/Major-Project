package com.help.dto;

public class UserCampaign {
    private int campaignId;
    private String campaignTitle, campaignDescription;
    private short status; // -1 = Inactive, 0 = Active, 1 = Completed
    private String imagePath1;

    public UserCampaign(int campaignId, String campaignTitle, String campaignDescription, short status, String imagePath1) {
        this.campaignId = campaignId;
        this.campaignTitle = campaignTitle;
        this.campaignDescription = campaignDescription;
        this.status = status;
        this.imagePath1 = imagePath1;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }
}
