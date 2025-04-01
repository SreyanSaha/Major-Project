package com.help.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(nullable = false, unique = true)
    private String userUsername;
    @Column(nullable = false)
    private String userPassword;
    @Column(nullable = false)
    private String userFirstName;
    @Column(nullable = false)
    private String userLastName;
    @Column(nullable = false, unique = true)
    private Long aadharCardNumber;
    @Column(nullable = false, unique = true)
    private String userEmailId;
    @Column(nullable = false, unique = true)
    private Long userPhoneNumber;
    @Column(unique = true)
    private String profileImagePath;
    private Long contributionPoints;
    private Long validRepostPoints;
    private short userStatus;// 0 -> inactive or timeout, 1 -> active, 2 -> delete, 3 -> blacklisted
    private LocalDateTime signupDateTime;
    private LocalDateTime timeOutEndTime;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> postList;

    public User() {}

    @PrePersist
    protected void onCreate(){
        this.contributionPoints=0l;
        this.validRepostPoints=0l;
        this.userStatus=1;// 0 -> inactive or timeout, 1 -> active, 2 -> delete, 3 -> blacklisted
        this.signupDateTime=LocalDateTime.now();
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

    public void setLongitude(Double Longitude) {
        this.longitude = Longitude;
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

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public short getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(short userStatus) {
        this.userStatus = userStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Long getAadharCardNumber() {
        return aadharCardNumber;
    }

    public void setAadharCardNumber(Long aadharCardNumber) {
        this.aadharCardNumber = aadharCardNumber;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public Long getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(Long userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public Long getContributionPoints() {
        return contributionPoints;
    }

    public void setContributionPoints(Long contributionPoints) {
        this.contributionPoints += contributionPoints;
    }

    public Long getValidRepostPoints() {
        return validRepostPoints;
    }

    public void setValidRepostPoints(Long validRepostPoints) {
        this.validRepostPoints += validRepostPoints;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
}
