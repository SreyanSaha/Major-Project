package com.help.model;

import jakarta.persistence.*;

@Entity
public class PostCommentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postCommentLogId;
    private int userId, postCommentId;
    private short log;//1 -> like, 0 -> disLike

    public PostCommentLog() {}

    public PostCommentLog(int userId, int postCommentId, short log) {
        this.userId = userId;
        this.postCommentId = postCommentId;
        this.log = log;
    }

    public int getPostCommentLogId() {
        return postCommentLogId;
    }

    public void setPostCommentLogId(int postCommentLogId) {
        this.postCommentLogId = postCommentLogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(int postCommentId) {
        this.postCommentId = postCommentId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
