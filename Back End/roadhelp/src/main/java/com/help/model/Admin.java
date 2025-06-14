package com.help.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adminId;
    private short adminRole;//1 -> admin, 2 -> super admin
    private short adminStatus;//0 -> inactive or timeout, 1 -> active
    private LocalDateTime signupDateTime;
    private LocalDateTime timeOutEndTime;
    @Column(nullable = false)
    private String adminFirstName;
    @Column(nullable = false)
    private String adminLastName;
    @Column(nullable = false)
    private String adminEmailId;
    @Column(nullable = false)
    private long adminPhoneNumber;
    @Column(nullable = false)
    private String profileImagePath;
    private Double latitude;
    private Double longitude;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String adminDepartment;//Public Works, Electricity, Water Treatment, Road Maintenance, Waste Management
    @Column(nullable = false)
    private String adminEmployeeId;//Company Name (Dropdown - Auto-fills based on Sector)
    @Column(nullable = false)
    private String adminCompanyName;
    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    private UserAuthData authData;

    public Admin() {}

    public UserAuthData getAuthData() {
        return authData;
    }

    public void setAuthData(UserAuthData authData) {
        this.authData = authData;
    }

    @PrePersist
    protected void onCreate(){
        this.adminRole=-1;//1 -> admin, 2 -> super admin
        this.adminStatus=0;//0 -> inactive or timeout, 1 -> active, 2 -> delete
        this.signupDateTime=LocalDateTime.now();
        this.country="India";
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
