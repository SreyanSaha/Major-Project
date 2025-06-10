package com.help.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserReportLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userReportLogId;
    private int reporterUserId;
    private int reportedUserId;
    private short log;// 1 for report

    public UserReportLog(int reporterUserId, int reportedUserId, short log) {
        this.reporterUserId = reporterUserId;
        this.reportedUserId = reportedUserId;
        this.log = log;
    }

    public int getUserReportLogId() {
        return userReportLogId;
    }

    public void setUserReportLogId(int userReportLogId) {
        this.userReportLogId = userReportLogId;
    }

    public int getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(int reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
