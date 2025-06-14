package com.help.service;

import com.help.dto.*;
import com.help.model.*;
import com.help.repository.*;
import com.help.validation.PostValidation;
import jakarta.transaction.Transactional;
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
    private final PostReportLogRepository postReportLogRepository;
    private final GeoService geoService;

    @Autowired
    public PostService(PostRepository postRepository, PostLogRepository postLogRepository, PostCommentRepository postCommentRepository, PostReportLogRepository postReportLogRepository,
                       PostCommentLogRepository postCommentLogRepository, PostValidation postValidation, UserRepository userRepository, GeoService geoService) {
        this.postRepository = postRepository;
        this.postLogRepository = postLogRepository;
        this.postCommentRepository = postCommentRepository;
        this.postCommentLogRepository = postCommentLogRepository;
        this.postValidation = postValidation;
        this.userRepository = userRepository;
        this.postReportLogRepository = postReportLogRepository;
        this.geoService=geoService;
    }

    @Transactional
    public String createPost(List<MultipartFile> images, Post post, String uname) {
        if(post.getStreet()==null || post.getStreet().isEmpty() ||
                post.getState()==null || post.getState().isEmpty() ||
                post.getPostalCode()==null || post.getPostalCode().isEmpty()){
            AddressDetails addressDetails=geoService.getAddressFromLatLng(post.getLatitude(), post.getLongitude());
            post.setStreet(addressDetails.getStreet());
            post.setState(addressDetails.getState());
            post.setCity(addressDetails.getCity());
            post.setPostalCode(addressDetails.getZip());
        }
        String msg=postValidation.isValidPostDetails(post);
        if(!msg.equals("Validated"))return msg;
        String username=SecurityContextHolder.getContext().getAuthentication().getName();String root=Paths.get("").toAbsolutePath().toString();
        if(!uname.equals(username))return "Invalid username!";
        Optional<User> user=userRepository.findByUsername(username);
        post.setUser(user.get());
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

    @Transactional
    public ServiceResponse<FullPostData> upVotePost(int postId) {
        if(!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Failed to up vote the post.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = userRepository.findByUsername(username).get().getUserId();
        Optional<PostLog> existingLogOpt = postLogRepository.findByUserIdAndPostId(userId, postId);
        Post post = postRepository.findById(postId).orElseThrow();
        if (existingLogOpt.isPresent()) {
            PostLog existingLog = existingLogOpt.get();
            if (existingLog.getLog() == 1) {
                // Already upVoted, remove upvote (toggle off)
                post.setUpVoteCount(post.getUpVoteCount() - 1);
                postLogRepository.delete(existingLog);
                return new ServiceResponse<>("Vote removed.",postRepository.findFullPostById(postId).get());
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
        return new ServiceResponse<>("Post up voted.",postRepository.findFullPostById(postId).get());
    }

    @Transactional
    public ServiceResponse<FullPostData> downVotePost(int postId) {
        if(!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Failed to down vote the post.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = userRepository.findByUsername(username).get().getUserId();
        Optional<PostLog> existingLogOpt = postLogRepository.findByUserIdAndPostId(userId, postId);
        Post post = postRepository.findById(postId).orElseThrow();
        if (existingLogOpt.isPresent()) {
            PostLog existingLog = existingLogOpt.get();
            if (existingLog.getLog() == 0) {
                // Already downVoted, remove downVote (toggle off)
                post.setDownVoteCount(post.getDownVoteCount() - 1);
                postLogRepository.delete(existingLog);
                return new ServiceResponse<>("Vote removed.",postRepository.findFullPostById(postId).get());
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

        return new ServiceResponse<>("Post down voted.",postRepository.findFullPostById(postId).get());
    }

    @Transactional
    public ServiceResponse<Optional<CommentData>> addComment(CommentWrapper commentWrapper) {
        if(!postValidation.isValidComment(commentWrapper.getCommentDescription()))return new ServiceResponse<>("Failed to create comment.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!username.equals(commentWrapper.getUname()))return new ServiceResponse<>("Failed to create comment.");
        Post post = postRepository.findById(commentWrapper.getPostId()).get();
        User user = userRepository.findByUsername(username).get();
        PostComment postComment=new PostComment();
        postComment.setCommentDescription(commentWrapper.getCommentDescription());
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setAuthorProfileName(user.getUserFirstName()+" "+user.getUserLastName());
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
        int postCommentId = postCommentRepository.save(postComment).getPostCommentId();
        return new ServiceResponse<>("Commented.",postCommentRepository.findCommentById(postCommentId, user.getUserId()));
    }

    public ServiceResponse<Page<CommentData>> findAllCommentsByPostId(int postId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("commentDateTime").descending());
        if(!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Failed to fetch the comments.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Page<CommentData> response = postCommentRepository.findAllCommentsByPostId(postId, pageRequest, userRepository.findByUsername(username).get().getUserId());
        return new ServiceResponse<>(response.getTotalPages()==0?"No additional comments found.":"",response);
    }

    @Transactional
    public ServiceResponse<Optional<CommentData>> upVoteComment(int commentId) {
        if(!postValidation.isValidNumeric(Integer.toString(commentId)))return new ServiceResponse<>("Failed to up vote the comment.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = userRepository.findByUsername(username).get().getUserId();
        PostComment comment = postCommentRepository.findById(commentId).get();
        Optional<PostCommentLog> existingLog = postCommentLogRepository.findByUserIdAndPostCommentId(userId, comment.getPostCommentId());

        if (existingLog.isEmpty()){
            PostCommentLog postCommentLog=new PostCommentLog(userId,(short)1);
            postCommentLog.setPostComment(comment);
            comment.setLikeCount(comment.getLikeCount()+1);
            postCommentLogRepository.save(postCommentLog);
            postCommentRepository.save(comment);
        }else{
            if(existingLog.get().getLog()==(short)1){// removing the like
                postCommentLogRepository.deleteById(existingLog.get().getPostCommentLogId());
                comment.setLikeCount(comment.getLikeCount()-1);
                postCommentRepository.save(comment);
                return new ServiceResponse<>("Voted removed.",postCommentRepository.findCommentById(commentId, userId));
            }else if(existingLog.get().getLog()==(short)0){// removing dislike and adding like
                existingLog.get().setLog((short)1);
                comment.setDisLikeCount(comment.getDisLikeCount()-1);
                comment.setLikeCount(comment.getLikeCount()+1);
                postCommentRepository.save(comment);
                postCommentLogRepository.save(existingLog.get());
            }
        }
        return new ServiceResponse<>("Comment up voted.",postCommentRepository.findCommentById(commentId, userId));
    }

    @Transactional
    public ServiceResponse<Optional<CommentData>> downVoteComment(int commentId) {
        if(!postValidation.isValidNumeric(Integer.toString(commentId)))return new ServiceResponse<>("Failed to down vote the comment.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = userRepository.findByUsername(username).get().getUserId();
        PostComment comment = postCommentRepository.findById(commentId).orElseThrow();
        Optional<PostCommentLog> existingLog = postCommentLogRepository.findByUserIdAndPostCommentId(userId, comment.getPostCommentId());

        if (existingLog.isEmpty()){
            PostCommentLog postCommentLog=new PostCommentLog(userId,(short)0);
            postCommentLog.setPostComment(comment);
            comment.setDisLikeCount(comment.getDisLikeCount()+1);
            postCommentLogRepository.save(postCommentLog);
            postCommentRepository.save(comment);
        }else{
            if(existingLog.get().getLog()==(short)1){// removing like adding dislike
                existingLog.get().setLog((short)0);
                comment.setLikeCount(comment.getLikeCount()-1);
                comment.setDisLikeCount(comment.getDisLikeCount()+1);
                postCommentLogRepository.save(existingLog.get());
                postCommentRepository.save(comment);
            }else if(existingLog.get().getLog()==(short)0){// removing dislike
                postCommentLogRepository.deleteById(existingLog.get().getPostCommentLogId());
                comment.setDisLikeCount(comment.getDisLikeCount()-1);
                postCommentRepository.save(comment);
                return new ServiceResponse<>("Vote removed.", postCommentRepository.findCommentById(commentId, userId));
            }
        }
        return new ServiceResponse<>("Comment down voted.", postCommentRepository.findCommentById(commentId, userId));
    }

    @Transactional
    public ServiceResponse<Boolean> deletePostById(int postId){
        if (!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Invalid post id.", false);
        Optional<Post> post=postRepository.findById(postId);
        if(post.isEmpty())return new ServiceResponse<>("Post not found.", false);
        else{
            String []imagePaths={
                    post.get().getImagePath1(),
                    post.get().getImagePath2(),
                    post.get().getImagePath3(),
                    post.get().getImagePath4(),
                    post.get().getImagePath5()
            };
            String []afterWorkImagePaths={
                    post.get().getAfterWorkImagePath1(),
                    post.get().getAfterWorkImagePath2(),
                    post.get().getAfterWorkImagePath3(),
                    post.get().getAfterWorkImagePath4(),
                    post.get().getAfterWorkImagePath5()
            };
            if(!deletePostAllImages(imagePaths,  afterWorkImagePaths))return new ServiceResponse<>("Failed to delete the post.", false);
        }
        User user=post.get().getUser();
        user.setCivicTrustScore((user.getCivicTrustScore()-25>=0)?user.getCivicTrustScore()-25:user.getCivicTrustScore());
        postLogRepository.deleteByPostId(postId);
        postReportLogRepository.deleteByPostId(postId);
        postCommentRepository.deleteAll(post.get().getPostCommentList());
        postRepository.deleteById(postId);
        userRepository.save(user);
        return new ServiceResponse<>("Post deleted.", true);
    }

    @Transactional
    public ServiceResponse<Boolean> deletePost(int postId){
        if (!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Invalid post id.", false);
        Optional<Post> post=postRepository.findById(postId);
        if(post.isEmpty())return new ServiceResponse<>("Post not found.", false);
        else{
            String []imagePaths={
                    post.get().getImagePath1(),
                    post.get().getImagePath2(),
                    post.get().getImagePath3(),
                    post.get().getImagePath4(),
                    post.get().getImagePath5()
            };
            String []afterWorkImagePaths={
                    post.get().getAfterWorkImagePath1(),
                    post.get().getAfterWorkImagePath2(),
                    post.get().getAfterWorkImagePath3(),
                    post.get().getAfterWorkImagePath4(),
                    post.get().getAfterWorkImagePath5()
            };
            if(!deletePostAllImages(imagePaths,  afterWorkImagePaths))return new ServiceResponse<>("Failed to delete the post.", false);
        }
        postLogRepository.deleteByPostId(postId);
        postReportLogRepository.deleteByPostId(postId);
        postCommentRepository.deleteAll(post.get().getPostCommentList());
        postRepository.deleteById(postId);
        return new ServiceResponse<>("Post deleted.", true);
    }

    private boolean deletePostAllImages(String []imagePaths, String []afterWorkImagePaths){
        String root=Paths.get("").toAbsolutePath().toString();
        for(String path:imagePaths){
            if(path==null || path.isEmpty())continue;
            try{Files.delete(Paths.get(root+"/allMedia"+path));}catch (Exception e){e.printStackTrace();return false;}
        }
        for(String path:afterWorkImagePaths){
            if(path==null || path.isEmpty())continue;
            try{Files.delete(Paths.get(root+"/allMedia"+path));}catch (Exception e){e.printStackTrace();return false;}
        }
        return true;
    }

    @Transactional
    public ServiceResponse<Optional<FullPostData>> reportPost(int postId){
        if(!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Failed to report the post.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findByUsername(username).get();
        Optional<PostReportLog> postReportLog = postReportLogRepository.findByUserIdAndPostId(user.getUserId(), postId);
        if(postReportLog.isPresent()){// removing the report
            post.setPostReports(post.getPostReports()-1);
            postReportLogRepository.deleteById(postReportLog.get().getPostReportLogId());
            postRepository.save(post);
            new ServiceResponse<>("Report removed.", postRepository.findFullPostById(postId));
        }else{// reporting the post 
            PostReportLog reportLog=new PostReportLog(user.getUserId(), post.getPostId(), (short)1);
            post.setPostReports(post.getPostReports()+1);
            postReportLogRepository.save(reportLog);
            postRepository.save(post);
        }
        return new ServiceResponse<>("Post reported.", postRepository.findFullPostById(postId));
    }

    @Transactional
    public ServiceResponse<Boolean> deleteComment(int commentId) {
        if(!postValidation.isValidNumeric(Integer.toString(commentId)))return new ServiceResponse<>("Failed to delete the comment.",false);
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        if(user.getUserId()!=postCommentRepository.findById(commentId).get().getUser().getUserId())
            return new ServiceResponse<>("User is not authorised to delete the comment.",false);
        Post post = postCommentRepository.findById(commentId).get().getPost();
        Optional<PostCommentLog> postCommentLog = postCommentLogRepository.findByUserIdAndPostCommentId(commentId, user.getUserId());
        post.setCommentCount(post.getCommentCount()-1);
        postCommentRepository.deleteById(commentId);
        postCommentLog.ifPresent(commentLog -> postCommentLogRepository.deleteById(commentLog.getPostCommentLogId()));
        postRepository.save(post);
        return new ServiceResponse<>("Comment successfully deleted.", true);
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

    public ServiceResponse<Optional<FullPostData>> getPostById(int postId) {
        if(!postValidation.isValidNumeric(Integer.toString(postId)))return new ServiceResponse<>("Invalid post id.");
        Optional<FullPostData> response = postRepository.findFullPostById(postId);
        return new ServiceResponse<Optional<FullPostData>>(response.isPresent() ? "" : "No post found.", response);
    }

    @Transactional
    public ServiceResponse<Boolean> editPost(EditPostData editPostData, int postId) {
        String msg=postValidation.isValidPostDetails(editPostData);boolean msg2=postValidation.isValidNumeric(Integer.toString(postId));
        if(!msg.equals("Validated") || !msg2)return new ServiceResponse<>(msg, false);
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByUsername(username);
        Optional<Post> existingPost=postRepository.findById(postId);

        if(existingPost.isEmpty() || existingPost.get().getUser().getUserId()!=user.get().getUserId())
            return new ServiceResponse<>("Invalid user or post.", false);

        existingPost.get().setCity(editPostData.getCity());
        existingPost.get().setState(editPostData.getState());
        existingPost.get().setStreet(editPostData.getStreet());
        existingPost.get().setPostTitle(editPostData.getPostTitle());
        existingPost.get().setPostDescription(editPostData.getPostDescription());
        postRepository.save(existingPost.get());
        return new ServiceResponse<>("updated.",true);
    }

    public ServiceResponse<Page<PostData>> getSearchedPosts(int page, int size, String searchString){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostData> list = postRepository.findAllBySearchString(searchString, pageRequest);
        return new ServiceResponse<>(list.getTotalPages()==0?"No posts are found.":"", list);
    }

    public ServiceResponse<FullPostData> getSearchedPosts(String searchString){
        List<FullPostData> list = postRepository.searchPosts(searchString);
        return new ServiceResponse<>(list.isEmpty()?"No posts are found.":"", list);
    }

    public ServiceResponse<FullPostData> getAllNonCompletedPosts() {
        List<FullPostData> response = postRepository.findAllNonCompletedPosts();
        return new ServiceResponse<>(response.isEmpty()?"No non-complete posts found.":"", response);
    }
}
