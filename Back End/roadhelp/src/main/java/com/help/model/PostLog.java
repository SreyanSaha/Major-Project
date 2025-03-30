package com.help.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PostLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postLogId;
    private int userId, postId;
    private short log = -1;//1 -> like, 0 -> disLike

    public PostLog() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostLogId() {
        return postLogId;
    }

    public void setPostLogId(int postLogId) {
        this.postLogId = postLogId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
