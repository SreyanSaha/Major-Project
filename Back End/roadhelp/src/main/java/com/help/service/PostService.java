package com.help.service;

import com.help.model.Post;
import com.help.model.PostComment;
import com.help.model.PostCommentLog;
import com.help.model.PostLog;
import com.help.repository.PostCommentLogRepository;
import com.help.repository.PostCommentRepository;
import com.help.repository.PostLogRepository;
import com.help.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostLogRepository postLogRepository;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    PostCommentLogRepository postCommentLogRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public String getPostLocation(int postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return String.format("Street: %s, City: %s, State: %s, Country: %s",
                post.getStreet(), post.getCity(), post.getState(), post.getCountry());
    }

    public List<Post> getNearbyPosts(double lat, double lon, double radius) {
        return postRepository.findByLatitudeBetweenAndLongitudeBetween( - radius, lat + radius, lon - radius, lon + radius);
    }

    public void upVotePost(int postId, int userId) {
        Optional<PostLog> existingLogOpt = postLogRepository.findByUserIdAndPostId(userId, postId);
        Post post = postRepository.findById(postId).orElseThrow();
        if (existingLogOpt.isPresent()) {
            PostLog existingLog = existingLogOpt.get();

            if (existingLog.getLog() == 1) {
                // Already upVoted, remove upvote (toggle off)
                post.setUpVoteCount(post.getUpVoteCount() - 1);
                postLogRepository.delete(existingLog);
            } else if (existingLog.getLog() == 0) {
                // Was downVoted, change to upvote
                post.setDownVoteCount(post.getDownVoteCount() - 1);
                post.setUpVoteCount(post.getUpVoteCount() + 1);
                existingLog.setLog((short) 1);
                postLogRepository.save(existingLog);
            }
        } else {
            // No vote yet, add upvote
            post.setUpVoteCount(post.getUpVoteCount() + 1);
            PostLog log = new PostLog();
            log.setUserId(userId);
            log.setPostId(postId);
            log.setLog((short) 1);
            postLogRepository.save(log);
        }

        postRepository.save(post);
    }

    public void downVotePost(int postId, int userId) {
        Optional<PostLog> existingLogOpt = postLogRepository.findByUserIdAndPostId(userId, postId);
        Post post = postRepository.findById(postId).orElseThrow();

        if (existingLogOpt.isPresent()) {
            PostLog existingLog = existingLogOpt.get();

            if (existingLog.getLog() == 0) {
                // Already downVoted, remove downVote (toggle off)
                post.setDownVoteCount(post.getDownVoteCount() - 1);
                postLogRepository.delete(existingLog);
            } else if (existingLog.getLog() == 1) {
                // Was upVoted, change to downVote
                post.setUpVoteCount(post.getUpVoteCount() - 1);
                post.setDownVoteCount(post.getDownVoteCount() + 1);
                existingLog.setLog((short) 0);
                postLogRepository.save(existingLog);
            }
        } else {
            // No vote yet, add downvote
            post.setDownVoteCount(post.getDownVoteCount() + 1);
            PostLog log = new PostLog();
            log.setUserId(userId);
            log.setPostId(postId);
            log.setLog((short) 0);
            postLogRepository.save(log);
        }

        postRepository.save(post);
    }


    public PostComment addComment(int postId, PostComment comment) {
        Post post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
        return postCommentRepository.save(comment);
    }

    public void upVoteComment(int commentId, int userId) {
        PostComment comment = postCommentRepository.findById(commentId).orElseThrow();
        Optional<PostCommentLog> existingLog = postCommentLogRepository.findByUserIdAndPostCommentId(userId, comment.getPostCommentId());
        if (existingLog.isEmpty()){
            PostCommentLog postCommentLog=new PostCommentLog(userId, commentId,(short)1);
            comment.setLikeCount(comment.getLikeCount()+1);
            postCommentLogRepository.save(postCommentLog);
            postCommentRepository.save(comment);
        }else if(!existingLog.isEmpty()){
            if(existingLog.get().getLog()==(short)1){
                existingLog.get().setLog((short)-1);
                comment.setLikeCount(comment.getLikeCount()-1);
            }else if(existingLog.get().getLog()==(short)0){
                existingLog.get().setLog((short)1);
                comment.setDisLikeCount(comment.getDisLikeCount()-1);
                comment.setLikeCount(comment.getLikeCount()+1);
            }else {
                existingLog.get().setLog((short)1);
                comment.setLikeCount(comment.getLikeCount()+1);
            }
            postCommentLogRepository.save(existingLog.get());
            postCommentRepository.save(comment);
        }
    }

    public void downVoteComment(int commentId, int userId) {
        PostComment comment = postCommentRepository.findById(commentId).orElseThrow();
        Optional<PostCommentLog> existingLog = postCommentLogRepository.findByUserIdAndPostCommentId(userId, comment.getPostCommentId());
        if (existingLog.isEmpty()){
            PostCommentLog postCommentLog=new PostCommentLog(userId, commentId,(short)0);
            comment.setDisLikeCount(comment.getDisLikeCount()+1);
            postCommentLogRepository.save(postCommentLog);
            postCommentRepository.save(comment);
        }else if(!existingLog.isEmpty()){
            if(existingLog.get().getLog()==(short)1){
                existingLog.get().setLog((short)0);
                comment.setLikeCount(comment.getLikeCount()-1);
                comment.setDisLikeCount(comment.getDisLikeCount()+1);
            }else if(existingLog.get().getLog()==(short)0){
                existingLog.get().setLog((short)-1);
                comment.setDisLikeCount(comment.getDisLikeCount()-1);
            }else {
                existingLog.get().setLog((short)0);
                comment.setDisLikeCount(comment.getDisLikeCount()+1);
            }


            postCommentLogRepository.save(existingLog.get());
            postCommentRepository.save(comment);
        }
    }

    public void deletePost(int postId){

    }
}
