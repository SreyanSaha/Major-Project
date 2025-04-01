package com.help.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PostCommentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postCommentLogId;
    private long userId, postCommentId;
    private short log = -1;//1 -> like, 0 -> disLike

    public PostCommentLog() {}

    public long getPostCommentLogId() {
        return postCommentLogId;
    }

    public void setPostCommentLogId(long postCommentLogId) {
        this.postCommentLogId = postCommentLogId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(long postCommentId) {
        this.postCommentId = postCommentId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
