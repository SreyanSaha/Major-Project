package com.help.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    @Column(nullable = false)
    private LocalDateTime postUploadDateTime;
    @Column(nullable = false, length = 500)
    private String postTitle;
    @Column(nullable = false, length = 1000)
    private String postDescription;
    private int upVoteCount, downVoteCount, commentCount;
    @Column(nullable = false)
    private String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5;
    private String afterWorkImagePath1, afterWorkImagePath2, afterWorkImagePath3, afterWorkImagePath4, afterWorkImagePath5;
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
    private String postalCode;
    private int postReports;
    private short postStatus;// -1 -> under review, 0 -> work in progress, 1 -> work done
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComment> postCommentList;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post() {}

    @PrePersist
    protected void onCreate(){
        this.postUploadDateTime = LocalDateTime.now();
        this.upVoteCount=this.commentCount=this.downVoteCount=0;
        this.postStatus=(short)-1;
        this.postReports=0;
        this.country="India";
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public LocalDateTime getPostUploadDateTime() {
        return postUploadDateTime;
    }

    public void setPostUploadDateTime(LocalDateTime postUploadDateTime) {
        this.postUploadDateTime = postUploadDateTime;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

    public String getAfterWorkImagePath1() {
        return afterWorkImagePath1;
    }

    public void setAfterWorkImagePath1(String afterWorkImagePath1) {
        this.afterWorkImagePath1 = afterWorkImagePath1;
    }

    public String getAfterWorkImagePath2() {
        return afterWorkImagePath2;
    }

    public void setAfterWorkImagePath2(String afterWorkImagePath2) {
        this.afterWorkImagePath2 = afterWorkImagePath2;
    }

    public String getAfterWorkImagePath3() {
        return afterWorkImagePath3;
    }

    public void setAfterWorkImagePath3(String afterWorkImagePath3) {
        this.afterWorkImagePath3 = afterWorkImagePath3;
    }

    public String getAfterWorkImagePath4() {
        return afterWorkImagePath4;
    }

    public void setAfterWorkImagePath4(String afterWorkImagePath4) {
        this.afterWorkImagePath4 = afterWorkImagePath4;
    }

    public String getAfterWorkImagePath5() {
        return afterWorkImagePath5;
    }

    public void setAfterWorkImagePath5(String afterWorkImagePath5) {
        this.afterWorkImagePath5 = afterWorkImagePath5;
    }

    public List<PostComment> getPostCommentList() {
        return postCommentList;
    }

    public void setPostCommentList(List<PostComment> postCommentList) {
        this.postCommentList = postCommentList;
    }

    public short getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(short postStatus) {
        this.postStatus = postStatus;
    }

    public int getPostReports() {
        return postReports;
    }

    public void setPostReports(int postReports) {
        this.postReports = postReports;
    }
}
