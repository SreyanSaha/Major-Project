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
    private int userId;
    @Column(nullable = false)
    private String authorProfileName, authorProfileImagePath;
    @Column(nullable = false)
    private LocalDateTime postUploadDateTime = LocalDateTime.now();
    @Column(nullable = false)
    private String postTitle, postDescription;
    private int upVoteCount = 0, downVoteCount = 0, commentCount = 0;
    @Column(nullable = false)
    private String imagePath1, imagePath2, imagePath3, imagePath4, imagePath5;
    private String afterWorkImagePath1, afterWorkImagePath2, afterWorkImagePath3, afterWorkImagePath4, afterWorkImagePath5;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postCommentList;

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
}
