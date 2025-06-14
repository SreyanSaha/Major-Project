package com.help.dto;

import java.time.LocalDateTime;

public class AdminProfile {
    private int adminId;
    private short adminRole;//1 -> admin, 2 -> super admin
    private short adminStatus;//0 -> inactive or timeout, 1 -> active, 2 -> delete, 3 -> blacklisted
    private LocalDateTime signupDateTime;
    private LocalDateTime timeOutEndTime;
    private String adminFirstName;
    private String adminLastName;
    private String adminEmailId;
    private long adminPhoneNumber;
    private String profileImagePath;
    private Double latitude;
    private Double longitude;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String adminDepartment;//Public Works, Electricity, Water Treatment, Road Maintenance, Waste Management
    private String adminEmployeeId;//Company Name (Dropdown - Auto-fills based on Sector)
    private String adminCompanyName;

    public AdminProfile(int adminId, short adminRole, short adminStatus, LocalDateTime signupDateTime, LocalDateTime timeOutEndTime, String adminFirstName,
                        String adminLastName, String adminEmailId, long adminPhoneNumber, String profileImagePath, Double latitude, Double longitude, String street,
                        String city, String state, String country, String zipCode, String adminDepartment, String adminEmployeeId, String adminCompanyName) {
        this.adminId = adminId;
        this.adminRole = adminRole;
        this.adminStatus = adminStatus;
        this.signupDateTime = signupDateTime;
        this.timeOutEndTime = timeOutEndTime;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
        this.adminEmailId = adminEmailId;
        this.adminPhoneNumber = adminPhoneNumber;
        this.profileImagePath = profileImagePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.adminDepartment = adminDepartment;
        this.adminEmployeeId = adminEmployeeId;
        this.adminCompanyName = adminCompanyName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public short getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(short adminRole) {
        this.adminRole = adminRole;
    }

    public short getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(short adminStatus) {
        this.adminStatus = adminStatus;
    }

    public LocalDateTime getSignupDateTime() {
        return signupDateTime;
    }

    public void setSignupDateTime(LocalDateTime signupDateTime) {
        this.signupDateTime = signupDateTime;
    }

    public LocalDateTime getTimeOutEndTime() {
        return timeOutEndTime;
    }

    public void setTimeOutEndTime(LocalDateTime timeOutEndTime) {
        this.timeOutEndTime = timeOutEndTime;
    }

    public String getAdminFirstName() {
        return adminFirstName;
    }

    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    public String getAdminLastName() {
        return adminLastName;
    }

    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    public String getAdminEmailId() {
        return adminEmailId;
    }

    public void setAdminEmailId(String adminEmailId) {
        this.adminEmailId = adminEmailId;
    }

    public long getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(long adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAdminDepartment() {
        return adminDepartment;
    }

    public void setAdminDepartment(String adminDepartment) {
        this.adminDepartment = adminDepartment;
    }

    public String getAdminEmployeeId() {
        return adminEmployeeId;
    }

    public void setAdminEmployeeId(String adminEmployeeId) {
        this.adminEmployeeId = adminEmployeeId;
    }

    public String getAdminCompanyName() {
        return adminCompanyName;
    }

    public void setAdminCompanyName(String adminCompanyName) {
        this.adminCompanyName = adminCompanyName;
    }
}
