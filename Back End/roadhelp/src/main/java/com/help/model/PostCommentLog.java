package com.help.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class PostCommentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postCommentLogId;
    private int userId;
    private short log;//1 -> like, 0 -> disLike
    @ManyToOne
    @JoinColumn(name = "postCommentId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PostComment postComment;

    public PostCommentLog() {}

    public PostCommentLog(int userId, short log) {
        this.userId = userId;
        this.log = log;
    }

    public PostComment getPostComment() {
        return postComment;
    }

    public void setPostComment(PostComment postComment) {
        this.postComment = postComment;
    }

    public int getPostCommentLogId() {
        return postCommentLogId;
    }

    public void setPostCommentLogId(int postCommentLogId) {
        this.postCommentLogId = postCommentLogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public short getLog() {
        return log;
    }

    public void setLog(short log) {
        this.log = log;
    }
}
