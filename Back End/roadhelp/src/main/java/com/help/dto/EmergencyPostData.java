package com.help.dto;

import java.time.LocalDateTime;

public class EmergencyPostData {
    private int emergencyPostId, civicTrustScore;
    private String imagePath1;
    private String emergencyPostTitle, emergencyPostDescription, authorName, profileImagePath;
    private LocalDateTime emergencyPostUploadDateTime;
    private short emergencyPostStatus;// 0 -> Unresolved, 1 -> resolved
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public EmergencyPostData(int emergencyPostId, String imagePath1, String emergencyPostTitle,
                             String emergencyPostDescription, String authorName, String profileImagePath,
                             LocalDateTime emergencyPostUploadDateTime, short emergencyPostStatus, int civicTrustScore, String street, String city, String state, String country, String zipCode) {
        this.emergencyPostId = emergencyPostId;
        this.imagePath1 = imagePath1;
        this.emergencyPostTitle = emergencyPostTitle;
        this.emergencyPostDescription = emergencyPostDescription;
        this.authorName = authorName;
        this.profileImagePath = profileImagePath;
        this.emergencyPostUploadDateTime = emergencyPostUploadDateTime;
        this.emergencyPostStatus = emergencyPostStatus;
        this.civicTrustScore = civicTrustScore;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }

    public EmergencyPostData(int emergencyPostId, String imagePath1, String emergencyPostTitle,
                             String emergencyPostDescription, short emergencyPostStatus){
        this.emergencyPostId = emergencyPostId;
        this.imagePath1 = imagePath1;
        this.emergencyPostTitle = emergencyPostTitle;
        this.emergencyPostDescription = emergencyPostDescription;
        this.emergencyPostStatus = emergencyPostStatus;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getCivicTrustScore() {
        return civicTrustScore;
    }

    public void setCivicTrustScore(int civicTrustScore) {
        this.civicTrustScore = civicTrustScore;
    }

    public int getEmergencyPostId() {
        return emergencyPostId;
    }

    public void setEmergencyPostId(int emergencyPostId) {
        this.emergencyPostId = emergencyPostId;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getEmergencyPostTitle() {
        return emergencyPostTitle;
    }

    public void setEmergencyPostTitle(String emergencyPostTitle) {
        this.emergencyPostTitle = emergencyPostTitle;
    }

    public String getEmergencyPostDescription() {
        return emergencyPostDescription;
    }

    public void setEmergencyPostDescription(String emergencyPostDescription) {
        this.emergencyPostDescription = emergencyPostDescription;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public LocalDateTime getEmergencyPostUploadDateTime() {
        return emergencyPostUploadDateTime;
    }

    public void setEmergencyPostUploadDateTime(LocalDateTime emergencyPostUploadDateTime) {
        this.emergencyPostUploadDateTime = emergencyPostUploadDateTime;
    }

    public short getEmergencyPostStatus() {
        return emergencyPostStatus;
    }

    public void setEmergencyPostStatus(short emergencyPostStatus) {
        this.emergencyPostStatus = emergencyPostStatus;
    }
}
