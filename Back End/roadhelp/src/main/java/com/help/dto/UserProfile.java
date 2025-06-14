package com.help.dto;

import java.time.LocalDateTime;

public class UserProfile {
    private int userId;
    private String userFirstName, userLastName, userEmailId;
    private Long userPhoneNumber;
    private String profileImagePath;
    private int civicTrustScore, userReports;
    private short userStatus;// 0 -> inactive or timeout, 1 -> active, 2 -> delete
    private LocalDateTime timeOutEndTime;
    private LocalDateTime signupDateTime;
    private String street,city,state,zipCode,country;

    public UserProfile(int userId, String userFirstName, String userLastName, String userEmailId, Long userPhoneNumber,
                       String profileImagePath, int civicTrustScore, short userStatus, LocalDateTime timeOutEndTime, String street,
                       String city, String state, String zipCode, String country, LocalDateTime signupDateTime) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmailId = userEmailId;
        this.userPhoneNumber = userPhoneNumber;
        this.profileImagePath = profileImagePath;
        this.civicTrustScore = civicTrustScore;
        this.userStatus = userStatus;
        this.timeOutEndTime = timeOutEndTime;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.signupDateTime = signupDateTime;
    }

    public UserProfile(int userId, String userFirstName, String userLastName, String userEmailId, Long userPhoneNumber,
                       String profileImagePath, int civicTrustScore, short userStatus, LocalDateTime timeOutEndTime, String street,
                       String city, String state, String zipCode, String country, LocalDateTime signupDateTime, int userReports) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmailId = userEmailId;
        this.userPhoneNumber = userPhoneNumber;
        this.profileImagePath = profileImagePath;
        this.civicTrustScore = civicTrustScore;
        this.userStatus = userStatus;
        this.timeOutEndTime = timeOutEndTime;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.signupDateTime = signupDateTime;
        this.userReports=userReports;
    }

    public UserProfile(int userId, String userFirstName, String userLastName, String profileImagePath,
                       int civicTrustScore, short userStatus, LocalDateTime timeOutEndTime, String city, String state,
                       String country, LocalDateTime signupDateTime) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.profileImagePath = profileImagePath;
        this.civicTrustScore = civicTrustScore;
        this.userStatus = userStatus;
        this.timeOutEndTime = timeOutEndTime;
        this.city = city;
        this.state = state;
        this.country = country;
        this.signupDateTime = signupDateTime;
    }

    public int getUserReports() {
        return userReports;
    }

    public void setUserReports(int userReports) {
        this.userReports = userReports;
    }

    public LocalDateTime getSignupDateTime() {
        return signupDateTime;
    }

    public void setSignupDateTime(LocalDateTime signupDateTime) {
        this.signupDateTime = signupDateTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public Long getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(Long userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public int getCivicTrustScore() {
        return civicTrustScore;
    }

    public void setCivicTrustScore(int civicTrustScore) {
        this.civicTrustScore = civicTrustScore;
    }

    public short getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(short userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getTimeOutEndTime() {
        return timeOutEndTime;
    }

    public void setTimeOutEndTime(LocalDateTime timeOutEndTime) {
        this.timeOutEndTime = timeOutEndTime;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
