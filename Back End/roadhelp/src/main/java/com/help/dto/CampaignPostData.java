package com.help.dto;

import java.time.LocalDateTime;

public class CampaignPostData {
    private int campaignId;
    private String campaignTitle, campaignDescription, campaignOrganizerName,
            campaignOrganizerProfileImagePath;
    private short status; // -1 = Inactive, 0 = Active, 1 = Completed
    private long campaignOrganizerContact;
    private String imagePath1;
    private int upVoteCount, downVoteCount, campaignReports, civicTrustScore;
    private LocalDateTime campaignCreationTime;
    private short campaignType;//Donation -> -1, Awareness -> 0, Volunteer -> 1

    public CampaignPostData(int campaignId, int civicTrustScore,String campaignTitle, String campaignDescription, String campaignOrganizerName, String campaignOrganizerProfileImagePath,
                            short status, long campaignOrganizerContact, String imagePath1, int upVoteCount, int downVoteCount,
                            int campaignReports, LocalDateTime campaignCreationTime, short campaignType) {
        this.campaignId = campaignId;
        this.campaignTitle = campaignTitle;
        this.campaignDescription = campaignDescription;
        this.campaignOrganizerName = campaignOrganizerName;
        this.campaignOrganizerProfileImagePath = campaignOrganizerProfileImagePath;
        this.status = status;
        this.campaignOrganizerContact = campaignOrganizerContact;
        this.imagePath1 = imagePath1;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.campaignReports = campaignReports;
        this.campaignCreationTime = campaignCreationTime;
        this.campaignType = campaignType;
        this.civicTrustScore = civicTrustScore;
    }

    public int getCivicTrustScore() {
        return civicTrustScore;
    }

    public void setCivicTrustScore(int civicTrustScore) {
        this.civicTrustScore = civicTrustScore;
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

    public String getCampaignOrganizerName() {
        return campaignOrganizerName;
    }

    public void setCampaignOrganizerName(String campaignOrganizerName) {
        this.campaignOrganizerName = campaignOrganizerName;
    }

    public String getCampaignOrganizerProfileImagePath() {
        return campaignOrganizerProfileImagePath;
    }

    public void setCampaignOrganizerProfileImagePath(String campaignOrganizerProfileImagePath) {
        this.campaignOrganizerProfileImagePath = campaignOrganizerProfileImagePath;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public long getCampaignOrganizerContact() {
        return campaignOrganizerContact;
    }

    public void setCampaignOrganizerContact(long campaignOrganizerContact) {
        this.campaignOrganizerContact = campaignOrganizerContact;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public int getCampaignReports() {
        return campaignReports;
    }

    public void setCampaignReports(int campaignReports) {
        this.campaignReports = campaignReports;
    }

    public LocalDateTime getCampaignCreationTime() {
        return campaignCreationTime;
    }

    public void setCampaignCreationTime(LocalDateTime campaignCreationTime) {
        this.campaignCreationTime = campaignCreationTime;
    }

    public short getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(short campaignType) {
        this.campaignType = campaignType;
    }
}
