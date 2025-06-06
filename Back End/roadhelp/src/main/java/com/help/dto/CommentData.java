package com.help.dto;

import java.time.LocalDateTime;

public class CommentData {
    private int postCommentId;
    private String authorProfileName, authorProfileImagePath;
    private LocalDateTime commentDateTime;
    private String commentDescription;
    private int likeCount, disLikeCount;
    private int userId;
    private boolean deletable;

    public CommentData(int postCommentId, String authorProfileName, String authorProfileImagePath, LocalDateTime commentDateTime, String commentDescription,
                       int likeCount, int disLikeCount, int userId, boolean deletable) {
        this.postCommentId = postCommentId;
        this.authorProfileName = authorProfileName;
        this.authorProfileImagePath = authorProfileImagePath;
        this.commentDateTime = commentDateTime;
        this.commentDescription = commentDescription;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.userId = userId;
        this.deletable = deletable;
    }

    public boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public int getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(int postCommentId) {
        this.postCommentId = postCommentId;
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

    public LocalDateTime getCommentDateTime() {
        return commentDateTime;
    }

    public void setCommentDateTime(LocalDateTime commentDateTime) {
        this.commentDateTime = commentDateTime;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDisLikeCount() {
        return disLikeCount;
    }

    public void setDisLikeCount(int disLikeCount) {
        this.disLikeCount = disLikeCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
