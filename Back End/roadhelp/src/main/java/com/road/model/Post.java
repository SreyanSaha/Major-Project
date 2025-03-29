package com.road.model;
import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int postId;
    @Column
    String postTitle;
    @Column
    String postDescription;
    @Column
    String imagePath1;
    @Column
    String imagePath2;
    @Column
    String imagePath3;
    @Column
    String imagePath4;
    @Column
    String imagePath5;
    @Column
    String afterWorkImagePath1;
    @Column
    String afterWorkImagePath2;
    @Column
    String afterWorkImagePath3;
    @Column
    String afterWorkImagePath4;
    @Column
    String afterWorkImagePath5;
    @Column
    int upVoteCount;
    @Column
    int downVoteCount;
    @Column
    int commentsCount;

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

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
