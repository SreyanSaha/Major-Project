package com.help.dto;

public class EditCampaignData {
    private int campaignId;
    private String campaignTitle, campaignDescription;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private short campaignType;//Donation -> -1, Awareness -> 0, Volunteer -> 1

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public short getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(short campaignType) {
        this.campaignType = campaignType;
    }
}
