package com.help.dto;

public class UserPost {
    private int postId;
    private String postTitle, postDescription, imagePath1;
    private short postStatus;

    public UserPost(int postId, String postTitle, String postDescription, String imagePath1, short postStatus) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.imagePath1 = imagePath1;
        this.postStatus = postStatus;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public short getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(short postStatus) {
        this.postStatus = postStatus;
    }
}
