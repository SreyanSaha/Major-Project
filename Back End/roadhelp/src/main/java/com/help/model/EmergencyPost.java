package com.help.model;
import jakarta.persistence.*;

@Entity
public class EmergencyPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emergencyPostId;
    @Column(nullable = false)
    private String authorProfileName, authorProfileImagePath;
    private String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5;
    private String audioFilePath;
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
    private short emergencyPostStatus;// -1 -> Unresolved, 0 -> work in progress, 1 -> resolved
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EmergencyPost() {}

    @PrePersist
    protected void onCreate(){
        this.emergencyPostStatus=(short)-1;
    }

    public int getEmergencyPostId() {
        return emergencyPostId;
    }

    public void setEmergencyPostId(int emergencyPostId) {
        this.emergencyPostId = emergencyPostId;
    }

    public String getAuthorProfileName() {
        return authorProfileName;
    }

    public void setAuthorProfileName(String authorProfileName) {
        this.authorProfileName = authorProfileName;
    }

    public String getAuthorProfileImagePath() {
        return authorProfileImagePath;
    }

    public void setAuthorProfileImagePath(String authorProfileImagePath) {
        this.authorProfileImagePath = authorProfileImagePath;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getImagePath4() {
        return imagePath4;
    }

    public void setImagePath4(String imagePath4) {
        this.imagePath4 = imagePath4;
    }

    public String getImagePath5() {
        return imagePath5;
    }

    public void setImagePath5(String imagePath5) {
        this.imagePath5 = imagePath5;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public short getEmergencyPostStatus() {
        return emergencyPostStatus;
    }

    public void setEmergencyPostStatus(short emergencyPostStatus) {
        this.emergencyPostStatus = emergencyPostStatus;
    }
}
