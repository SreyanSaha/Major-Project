package com.help.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postCommentId;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private String authorProfileName, authorProfileImagePath;
    private LocalDateTime commentDateTime = LocalDateTime.now();
    @Column(nullable = false)
    private String commentDescription;
    private int likeCount = 0, disLikeCount = 0;
    private int replyCount = 0;
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<PostComment> commentReplies;

    public PostComment() {}

    public Post getPost() {
        return post;
    }

    public PostComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(PostComment parentComment) {
        this.parentComment = parentComment;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<PostComment> getCommentReplies() {
        return commentReplies;
    }

    public void setCommentReplies(List<PostComment> commentReplies) {
        this.commentReplies = commentReplies;
    }

    public int getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(int postCommentId) {
        this.postCommentId = postCommentId;
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

    public LocalDateTime getCommentDateTime() {
        return commentDateTime;
    }

    public void setCommentDateTime(LocalDateTime commentDateTime) {
        this.commentDateTime = commentDateTime;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDisLikeCount() {
        return disLikeCount;
    }

    public void setDisLikeCount(int disLikeCount) {
        this.disLikeCount = disLikeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
}
