package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.UserPost;
import com.help.jwt.service.UserAuthDataService;
import com.help.model.Post;
import com.help.model.PostComment;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        List<UserPost> posts=postService.getAllPostsOfUser();
        if(posts.isEmpty())return ResponseEntity.status(HttpStatus.OK).body("No posts found.");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(posts);
    }

//    @GetMapping("/nearby")
//    public ResponseEntity<?> getNearbyPosts(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("rad") double radius,
//                                            @RequestHeader("Authorization") String tokenHeader,
//                                            @RequestHeader("Username") String username) {
//        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
//        return ResponseEntity.ok().body(postService.getNearbyPosts(lat, lon, radius));
//    }

    @PostMapping("/upVote/{postId}")
    public ResponseEntity<?> upVote(@PathVariable int postId,
                                    @RequestHeader("Username") String username) {
        postService.upVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Up Voted");
    }

    @PostMapping("/downVote/{postId}")
    public ResponseEntity<?> downVote(@PathVariable int postId, @RequestHeader("Username") String username) {
        postService.downVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Down Voted");
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> addComment(@PathVariable int postId, @RequestBody PostComment comment, @RequestHeader("Username") String username) {
        return ResponseEntity.ok().body(postService.addComment(postId, comment));
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
