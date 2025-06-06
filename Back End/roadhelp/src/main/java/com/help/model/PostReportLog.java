package com.help.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PostReportLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postReportLogId;
    private int userId, postId;
    private short log;//1 -> reported

    public PostReportLog() {}

    public PostReportLog(int userId, int postId, short log) {
        this.userId = userId;
        this.postId = postId;
        this.log = log;
    }

    public int getPostReportLogId() {
        return postReportLogId;
    }

    public void setPostReportLogId(int postReportLogId) {
        this.postReportLogId = postReportLogId;
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

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
