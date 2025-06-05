package com.help.dto;

import com.help.model.User;

import java.time.LocalDateTime;

public class FullPostData {
    private int postId;
    private String authorProfileName, profileImagePath;
    private LocalDateTime postUploadDateTime;
    private String postTitle, postDescription;
    private int upVoteCount, downVoteCount, commentCount;
    private String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5;
    private String afterWorkImagePath1, afterWorkImagePath2, afterWorkImagePath3, afterWorkImagePath4, afterWorkImagePath5;
    private Double latitude;
    private Double longitude;
    private String street, city, state, country, postalCode;
    private int postReports, civicTrustScore;
    private short postStatus;// -1 -> under review, 0 -> work in progress, 1 -> work done
    private int userId;

    public FullPostData(int postId, String authorProfileName, LocalDateTime postUploadDateTime, String postTitle, String postDescription, int upVoteCount,
                        int downVoteCount, int commentCount, String imagePath1, String imagePath2, String imagePath3, String imagePath4, String imagePath5, String afterWorkImagePath1,
                        String afterWorkImagePath2, String afterWorkImagePath3, String afterWorkImagePath4, String afterWorkImagePath5, Double latitude, Double longitude, String street, String city,
                        String state, String country, String postalCode, int postReports, short postStatus, int userId, String profileImagePath, int civicTrustScore) {
        this.postId = postId;
        this.authorProfileName = authorProfileName;
        this.postUploadDateTime = postUploadDateTime;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.commentCount = commentCount;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.imagePath4 = imagePath4;
        this.imagePath5 = imagePath5;
        this.afterWorkImagePath1 = afterWorkImagePath1;
        this.afterWorkImagePath2 = afterWorkImagePath2;
        this.afterWorkImagePath3 = afterWorkImagePath3;
        this.afterWorkImagePath4 = afterWorkImagePath4;
        this.afterWorkImagePath5 = afterWorkImagePath5;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.postReports = postReports;
        this.postStatus = postStatus;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAuthorProfileName() {
        return authorProfileName;
    }

    public void setAuthorProfileName(String authorProfileName) {
        this.authorProfileName = authorProfileName;
    }

    public LocalDateTime getPostUploadDateTime() {
        return postUploadDateTime;
    }

    public void setPostUploadDateTime(LocalDateTime postUploadDateTime) {
        this.postUploadDateTime = postUploadDateTime;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

    public String getAfterWorkImagePath1() {
        return afterWorkImagePath1;
    }

    public void setAfterWorkImagePath1(String afterWorkImagePath1) {
        this.afterWorkImagePath1 = afterWorkImagePath1;
    }

    public String getAfterWorkImagePath2() {
        return afterWorkImagePath2;
    }

    public void setAfterWorkImagePath2(String afterWorkImagePath2) {
        this.afterWorkImagePath2 = afterWorkImagePath2;
    }

    public String getAfterWorkImagePath3() {
        return afterWorkImagePath3;
    }

    public void setAfterWorkImagePath3(String afterWorkImagePath3) {
        this.afterWorkImagePath3 = afterWorkImagePath3;
    }

    public String getAfterWorkImagePath4() {
        return afterWorkImagePath4;
    }

    public void setAfterWorkImagePath4(String afterWorkImagePath4) {
        this.afterWorkImagePath4 = afterWorkImagePath4;
    }

    public String getAfterWorkImagePath5() {
        return afterWorkImagePath5;
    }

    public void setAfterWorkImagePath5(String afterWorkImagePath5) {
        this.afterWorkImagePath5 = afterWorkImagePath5;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public int getPostReports() {
        return postReports;
    }

    public void setPostReports(int postReports) {
        this.postReports = postReports;
    }

    public short getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(short postStatus) {
        this.postStatus = postStatus;
    }
}
