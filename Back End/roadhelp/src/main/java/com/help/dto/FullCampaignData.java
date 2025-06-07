package com.help.dto;

import java.time.LocalDateTime;

public class FullCampaignData {
    private int campaignId;
    private String campaignTitle, campaignDescription, campaignOrganizerName,
            campaignOrganizerEmail, profileImagePath;
    private short status; // -1 = Inactive, 0 = Active, 1 = Completed
    private long campaignOrganizerContact;
    private String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5, upiImage;
    private String street;
    private int upVoteCount, downVoteCount, campaignReports;
    private String city, state, country, postalCode;
    private LocalDateTime campaignCreationTime;
    private short campaignType;//Donation -> -1, Awareness -> 0, Volunteer -> 1
    private int civicTrustScore, userId;

    public FullCampaignData(int campaignId, String campaignTitle, String campaignDescription, String campaignOrganizerName, String campaignOrganizerEmail, short status,
                            long campaignOrganizerContact, String imagePath1, String imagePath2, String imagePath3, String imagePath4, String imagePath5, String upiImage, String street,
                            int upVoteCount, int downVoteCount, int campaignReports, String city, String state, String country, String postalCode, LocalDateTime campaignCreationTime,
                            short campaignType, String profileImagePath, int civicTrustScore, int userId) {
        this.campaignId = campaignId;
        this.campaignTitle = campaignTitle;
        this.campaignDescription = campaignDescription;
        this.campaignOrganizerName = campaignOrganizerName;
        this.campaignOrganizerEmail = campaignOrganizerEmail;
        this.status = status;
        this.campaignOrganizerContact = campaignOrganizerContact;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.imagePath4 = imagePath4;
        this.imagePath5 = imagePath5;
        this.upiImage = upiImage;
        this.street = street;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.campaignReports = campaignReports;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.campaignCreationTime = campaignCreationTime;
        this.campaignType = campaignType;
        this.userId = userId;
        this.profileImagePath = profileImagePath;
        this.civicTrustScore = civicTrustScore;
    }

    public int getCivicTrustScore() {
        return civicTrustScore;
    }

    public void setCivicTrustScore(int civicTrustScore) {
        this.civicTrustScore = civicTrustScore;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
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

    public String getCampaignOrganizerEmail() {
        return campaignOrganizerEmail;
    }

    public void setCampaignOrganizerEmail(String campaignOrganizerEmail) {
        this.campaignOrganizerEmail = campaignOrganizerEmail;
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

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getImagePath4() {
        return imagePath4;
    }

    public void setImagePath4(String imagePath4) {
        this.imagePath4 = imagePath4;
    }

    public String getImagePath5() {
        return imagePath5;
    }

    public void setImagePath5(String imagePath5) {
        this.imagePath5 = imagePath5;
    }

    public String getUpiImage() {
        return upiImage;
    }

    public void setUpiImage(String upiImage) {
        this.upiImage = upiImage;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
