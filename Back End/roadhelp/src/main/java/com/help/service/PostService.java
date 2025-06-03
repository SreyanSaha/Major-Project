package com.help.service;

import com.help.dto.PostData;
import com.help.dto.ServiceResponse;
import com.help.dto.UserPost;
import com.help.model.*;
import com.help.repository.*;
import com.help.validation.PostValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostLogRepository postLogRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLogRepository postCommentLogRepository;
    private final PostValidation postValidation;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostLogRepository postLogRepository, PostCommentRepository postCommentRepository,
                       PostCommentLogRepository postCommentLogRepository, PostValidation postValidation, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postLogRepository = postLogRepository;
        this.postCommentRepository = postCommentRepository;
        this.postCommentLogRepository = postCommentLogRepository;
        this.postValidation = postValidation;
        this.userRepository = userRepository;
    }

    public String createPost(List<MultipartFile> images, Post post, String uname) {
        String msg=postValidation.isValidPostDetails(post);
        if(!msg.equals("Validated"))return msg;
        String username=SecurityContextHolder.getContext().getAuthentication().getName();String root=Paths.get("").toAbsolutePath().toString();
        if(!uname.equals(username))return "Invalid username!";
        Optional<User> user=userRepository.findByUsername(username);
        post.setUser(user.get());
        post.setAuthorProfileName(user.get().getUserFirstName()+" "+user.get().getUserLastName());
        String []postImagePaths=savePostImages(images, root);
        if(postImagePaths==null)return "Failed to create post.";
        post.setImagePath1(postImagePaths[0].replace(root+"\\allMedia",""));
        post.setImagePath2(postImagePaths[1].replace(root+"\\allMedia",""));
        post.setImagePath3(postImagePaths[2].replace(root+"\\allMedia",""));
        post.setImagePath4(postImagePaths[3].replace(root+"\\allMedia",""));
        post.setImagePath5(postImagePaths[4].replace(root+"\\allMedia",""));
        postRepository.save(post);
        return "created";
    }

    private String[] savePostImages(List<MultipartFile> images, String root){
        String []imagePaths=new String[5];int count=0;
        try{
            for(MultipartFile image:images){
                if(image.isEmpty() || image.getSize() > (5 * 1024 * 1024))return null;
                Path postImagePath=Paths.get(root+"/allMedia/postImages");
                if(!Files.exists(postImagePath))Files.createDirectories(postImagePath);
                Path uploadPath=postImagePath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+image.getOriginalFilename());
                imagePaths[count++]=uploadPath.toString();
                image.transferTo(uploadPath.toFile());
            }
            return imagePaths;
        }catch (Exception e){
            e.printStackTrace();
            for(String path:imagePaths) try{Files.delete(Paths.get(path));}catch (Exception e1){System.out.println(e1.toString());}
        }
        return null;
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

    public boolean deletePost(int postId, String username){
        if(userRepository.findByUsername(username).get().getUserId()!=postRepository.findById(postId).get().getUser().getUserId())return false;
        postRepository.deleteById(postId);
        return true;
    }

    public Post findPostByTitle(String search) {
        return postRepository.findPostByPostTitle(search);
    }

    public boolean deleteComment(int commentId, String username) {
        if(userRepository.findByUsername(username).get().getUserId()!=postCommentRepository.findById(commentId).get().getUser().getUserId())return false;
        postCommentRepository.deleteById(commentId);
        return true;
    }

    public ServiceResponse<PostData> getLimitedPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("postUploadDateTime").descending());
        List<PostData> list = postRepository.findLimitedPosts(pageRequest);
        return new ServiceResponse<PostData>(list.isEmpty()?"No posts are found.":"Please login to view and access all the posts.",list);
    }

    public ServiceResponse<Page<PostData>> getAllPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("postUploadDateTime").descending());
        Page<PostData> list = postRepository.findAllByOrderByPostUploadDateTimeDesc(pageRequest);
        return new ServiceResponse<>(list.getTotalPages()==0?"No additional posts are found.":"", list);
    }

    public ServiceResponse<UserPost> getAllPostsOfUser() {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserPost> list = postRepository.findAllPostsOfUser(username);
        return new ServiceResponse<UserPost>(list.isEmpty()?"No posts are found.":"",list);
    }
}
