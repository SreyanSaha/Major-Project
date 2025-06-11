package com.help.dto;

import java.time.LocalDateTime;

public class SubscriptionDetails {
    private int userSubscriptionId;
    private int userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private short log;// 1 for AI

    public SubscriptionDetails(int userSubscriptionId, int userId, LocalDateTime startDate, LocalDateTime endDate, short log) {
        this.userSubscriptionId = userSubscriptionId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.log = log;
    }

    public int getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(int userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }

}
