package com.help.dto;

import java.time.LocalDateTime;

public class PostData {
    private int postId;
    private String authorProfileName, authorProfileImagePath;
    private LocalDateTime postUploadDateTime;
    private String postTitle, postDescription;
    private int upVoteCount, downVoteCount, commentCount, postReports, civicTrustScore;
    private String imagePath1;
    private short postStatus;// -1 -> under review, 0 -> work in progress, 1 -> work done

    public PostData(int postId, int civicTrustScore, String authorProfileName, String authorProfileImagePath, LocalDateTime postUploadDateTime, String postTitle,
                    String postDescription, int upVoteCount, int downVoteCount, int commentCount, int postReports, String imagePath1, short postStatus) {
        this.postId = postId;
        this.authorProfileName = authorProfileName;
        this.authorProfileImagePath = authorProfileImagePath;
        this.postUploadDateTime = postUploadDateTime;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.commentCount = commentCount;
        this.postReports = postReports;
        this.imagePath1 = imagePath1;
        this.postStatus = postStatus;
        this.civicTrustScore = civicTrustScore;
    }

    public int getCivicTrustScore() {
        return civicTrustScore;
    }

    public void setCivicTrustScore(int civicTrustScore) {
        this.civicTrustScore = civicTrustScore;
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

    public String getAuthorProfileImagePath() {
        return authorProfileImagePath;
    }

    public void setAuthorProfileImagePath(String authorProfileImagePath) {
        this.authorProfileImagePath = authorProfileImagePath;
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

    public int getPostReports() {
        return postReports;
    }

    public void setPostReports(int postReports) {
        this.postReports = postReports;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public short getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(short postStatus) {
        this.postStatus = postStatus;
    }
}
