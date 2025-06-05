package com.help.model;

import jakarta.persistence.*;

@Entity
public class PostLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postLogId;
    private long userId, postId;
    private short log;//1 -> like, 0 -> disLike

    public PostLog() {}

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPostLogId() {
        return postLogId;
    }

    public void setPostLogId(long postLogId) {
        this.postLogId = postLogId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
