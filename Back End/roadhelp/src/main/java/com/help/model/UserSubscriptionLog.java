package com.help.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscription_log")
public class UserSubscriptionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userSubscriptionId;
    @Column(name = "user_id") // must match DB column
    private int userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private short log;// 1 for AI
    private String paymentId, paymentOrderId;

    public UserSubscriptionLog() {
    }

    public UserSubscriptionLog(int userId, short log, String paymentId, String paymentOrderId) {
        this.userId = userId;
        this.log = log;
        this.paymentId = paymentId;
        this.paymentOrderId = paymentOrderId;
    }

    @PrePersist
    protected void onCreate(){
        this.startDate=LocalDateTime.now();
        this.endDate=LocalDateTime.now().plusMonths(1);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(int userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
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
