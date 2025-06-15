package com.help.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int campaignId;
    @Column(nullable = false, length = 500)
    private String campaignTitle;
    @Column(nullable = false, length = 1000)
    private String campaignDescription;
    @Column(nullable = false)
    private String campaignOrganizerEmail;
    private short status; // -1 = Inactive, 0 = Active, 1 = Completed
    @Column(nullable = false)
    private long campaignOrganizerContact;
    @Column(nullable = false)
    private String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5;
    private String upiImage;
    @Column(nullable = false)
    private String street;
    private int upVoteCount, downVoteCount, campaignReports;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    private String country;
    @Column(nullable = false)
    private String postalCode;
    private LocalDateTime campaignCreationTime;
    @Column(nullable = false)
    private short campaignType;//Donation -> -1, Awareness -> 0, Volunteer -> 1
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Campaign() {
    }

    @PrePersist
    protected void onCreate(){
        this.upVoteCount=this.downVoteCount=0;
        this.campaignReports=0;
        this.status=-1;
        this.country="India";
        this.campaignCreationTime=LocalDateTime.now();
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

    public String getUpiImage() {
        return upiImage;
    }

    public void setUpiImage(String upiImage) {
        this.upiImage = upiImage;
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

    public long getCampaignOrganizerContact() {
        return campaignOrganizerContact;
    }

    public void setCampaignOrganizerContact(long campaignOrganizerContact) {
        this.campaignOrganizerContact = campaignOrganizerContact;
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

    public short getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(short campaignType) {
        this.campaignType = campaignType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
