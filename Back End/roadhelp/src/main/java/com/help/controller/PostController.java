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
        catch(Exception e){e.printStackTrace();return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create post.");}
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

//    @GetMapping("/nearby")
//    public ResponseEntity<?> getNearbyPosts(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("rad") double radius,
//                                            @RequestHeader("Authorization") String tokenHeader,
//                                            @RequestHeader("Username") String username) {
//        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
//        return ResponseEntity.ok().body(postService.getNearbyPosts(lat, lon, radius));
//    }

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

    @PostMapping("/comment/upVote/{commentId}")
    public ResponseEntity<?> upVoteComment(@PathVariable int commentId,@RequestHeader("Username") String username) {
        postService.upVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Comment Up Voted");
    }

    @PostMapping("/comment/downVote/{commentId}")
    public ResponseEntity<?> downVoteComment(@PathVariable int commentId, @RequestHeader("Username") String username) {
        postService.downVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Comment Up Voted");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId, @RequestHeader("Username") String username){

        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/delete/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @RequestHeader("Username") String username){

        return ResponseEntity.ok().body("");
    }

    @GetMapping("/search/post")
    public ResponseEntity<?> searchPostByTitle(@RequestParam("search") String search, @RequestHeader("Username") String username){
        return ResponseEntity.ok().body("");
    }
}
