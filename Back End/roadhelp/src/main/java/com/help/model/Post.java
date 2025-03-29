package com.help.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private String postTitle;
    @Column(nullable = false)
    private String postDescription;
    @Column(nullable = false)
    private String authorName;
    @Column
    private String authorProfileImgPath;
    @Column
    private int upVoteCount;
    @Column
    private int downVoteCount;
    @Column
    private int commentCount;
    @Column
    private String imagePath1;
    @Column
    private String imagePath2;
    @Column
    private String imagePath3;
    @Column
    private String imagePath4;
    @Column
    private String imagePath5;
    @Column
    private String imageAfterWorkPath1;
    @Column
    private String imageAfterWorkPath2;
    @Column
    private String imageAfterWorkPath3;
    @Column
    private String imageAfterWorkPath4;
    @Column
    private String imageAfterWorkPath5;
    @Column(nullable = false)
    private LocalDateTime postUploadDateTime;

    public Post() {}

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorProfileImgPath() {
        return authorProfileImgPath;
    }

    public void setAuthorProfileImgPath(String authorProfileImgPath) {
        this.authorProfileImgPath = authorProfileImgPath;
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

    public String getImageAfterWorkPath1() {
        return imageAfterWorkPath1;
    }

    public void setImageAfterWorkPath1(String imageAfterWorkPath1) {
        this.imageAfterWorkPath1 = imageAfterWorkPath1;
    }

    public String getImageAfterWorkPath2() {
        return imageAfterWorkPath2;
    }

    public void setImageAfterWorkPath2(String imageAfterWorkPath2) {
        this.imageAfterWorkPath2 = imageAfterWorkPath2;
    }

    public String getImageAfterWorkPath3() {
        return imageAfterWorkPath3;
    }

    public void setImageAfterWorkPath3(String imageAfterWorkPath3) {
        this.imageAfterWorkPath3 = imageAfterWorkPath3;
    }

    public String getImageAfterWorkPath4() {
        return imageAfterWorkPath4;
    }

    public void setImageAfterWorkPath4(String imageAfterWorkPath4) {
        this.imageAfterWorkPath4 = imageAfterWorkPath4;
    }

    public String getImageAfterWorkPath5() {
        return imageAfterWorkPath5;
    }

    public void setImageAfterWorkPath5(String imageAfterWorkPath5) {
        this.imageAfterWorkPath5 = imageAfterWorkPath5;
    }

    public LocalDateTime getPostUploadDateTime() {
        return postUploadDateTime;
    }

    public void setPostUploadDateTime(LocalDateTime postUploadDateTime) {
        this.postUploadDateTime = postUploadDateTime;
    }
}
