package com.help.dto;

import java.time.LocalDateTime;

public class UserSearchData {
    private int userId;
    private String userFirstName, userLastName, profileImagePath;
    private int civicTrustScore;
    private long postCount, campaignCount;
    private short userStatus;// 0 -> inactive or timeout, 1 -> active, 2 -> delete, 3 -> blacklisted
    private LocalDateTime signupDateTime;

    public UserSearchData(int userId, String userFirstName, String userLastName, String profileImagePath,
                          int civicTrustScore, long postCount, long campaignCount, short userStatus, LocalDateTime signupDateTime) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.profileImagePath = profileImagePath;
        this.civicTrustScore = civicTrustScore;
        this.postCount = postCount;
        this.campaignCount = campaignCount;
        this.userStatus = userStatus;
        this.signupDateTime = signupDateTime;
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

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }

    public long getCampaignCount() {
        return campaignCount;
    }

    public void setCampaignCount(long campaignCount) {
        this.campaignCount = campaignCount;
    }

    public short getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(short userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getSignupDateTime() {
        return signupDateTime;
    }

    public void setSignupDateTime(LocalDateTime signupDateTime) {
        this.signupDateTime = signupDateTime;
    }
}
