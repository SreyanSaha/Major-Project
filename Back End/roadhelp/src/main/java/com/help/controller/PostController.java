package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.*;
import com.help.jwt.service.UserAuthDataService;
import com.help.model.Post;
import com.help.model.PostComment;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final UserAuthDataService userAuthDataService;

    @Autowired
    public PostController(PostService postService,
                          UserService userService,
                          UserAuthDataService userAuthDataService) {
        this.postService = postService;
        this.userService = userService;
        this.userAuthDataService = userAuthDataService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestPart("post") String postJson,
                                        @RequestPart("images") List<MultipartFile> images,
                                        @RequestPart("uname")String uname){
        Post post=null;
        try{post=new ObjectMapper().readValue(postJson, Post.class);}
        catch(Exception e){e.fillInStackTrace();return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create post.");}
        String response=postService.createPost(images,post,uname);
        if(!response.equals("created")) return ResponseEntity.status(HttpStatus.OK).body(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/all-posts")
    public ResponseEntity<?> getUserAllPosts(){
        ServiceResponse<UserPost> response=postService.getAllPostsOfUser();
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/upVote")
    public ResponseEntity<?> upVote(@RequestBody int postId) {
        ServiceResponse<FullPostData> response = postService.upVotePost(postId);
        if(response.getObject()==null)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/downVote")
    public ResponseEntity<?> downVote(@RequestBody int postId) {
        ServiceResponse<FullPostData> response = postService.downVotePost(postId);
        if(response.getObject()==null)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("add/comment")
    public ResponseEntity<?> addComment(@RequestBody CommentWrapper commentWrapper) {
        ServiceResponse<Optional<CommentData>> response = postService.addComment(commentWrapper);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all/comments/{postId}/page/{page}/size/{size}")
    public ResponseEntity<?> addComment(@PathVariable int postId, @PathVariable int page, @PathVariable int size) {
        ServiceResponse<Page<CommentData>> response = postService.findAllCommentsByPostId(postId, page, size);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/comment/upVote")
    public ResponseEntity<?> upVoteComment(@RequestBody int commentId) {
        ServiceResponse<Optional<CommentData>> response = postService.upVoteComment(commentId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/comment/downVote")
    public ResponseEntity<?> downVoteComment(@RequestBody int commentId) {
        ServiceResponse<Optional<CommentData>> response = postService.downVoteComment(commentId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportPost(@RequestBody int postId){
        ServiceResponse<Optional<FullPostData>> response = postService.reportPost(postId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId){
        ServiceResponse<Boolean> response=postService.deletePost(postId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId){
        ServiceResponse<Boolean> response = postService.deleteComment(commentId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editPost(@RequestBody EditPostData editPostData){
        ServiceResponse<Boolean> response = postService.editPost(editPostData, editPostData.getPostId());
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/page/{page}/size/{size}/{searchString}")
    public ResponseEntity<?> searchPost(@PathVariable int page, @PathVariable int size, @PathVariable String searchString){
        ServiceResponse<Page<PostData>> response=postService.getSearchedPosts(page, size, searchString);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
