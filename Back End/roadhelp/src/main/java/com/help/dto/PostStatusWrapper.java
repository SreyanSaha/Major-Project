package com.help.dto;

public class PostStatusWrapper {
    private int postId;
    private short postStatus;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public short getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(short postStatus) {
        this.postStatus = postStatus;
    }
}
