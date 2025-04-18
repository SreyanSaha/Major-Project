package com.help.controller;

import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.jwt.service.UserAuthDataService;
import com.help.model.EmergencyPost;
import com.help.model.Post;
import com.help.model.PostComment;
import com.help.service.EmergencyPostService;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final UserAuthDataService userAuthDataService;
    private final EmergencyPostService emergencyPostService;

    @Autowired
    public PostController(PostService postService, JwtService jwtService, CustomUserDetailsService customUserDetailsService,
                          UserService userService, UserAuthDataService userAuthDataService, EmergencyPostService emergencyPostService) {
        this.postService = postService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        this.userAuthDataService = userAuthDataService;
        this.emergencyPostService = emergencyPostService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        post.setUser(userService.getUserByAuthId(userAuthDataService.getAuthId(username)));
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post,username));
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyPosts(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("rad") double radius,
                                            @RequestHeader("Authorization") String tokenHeader,
                                            @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.getNearbyPosts(lat, lon, radius));
    }

    @PostMapping("/upVote/{postId}")
    public ResponseEntity<?> upVote(@PathVariable int postId, @RequestHeader("Authorization") String tokenHeader,
                                    @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.upVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Up Voted");
    }

    @PostMapping("/downVote/{postId}")
    public ResponseEntity<?> downVote(@PathVariable int postId, @RequestHeader("Authorization") String tokenHeader,
                                      @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.downVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Down Voted");
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> addComment(@PathVariable int postId, @RequestBody PostComment comment, @RequestHeader("Authorization") String tokenHeader,
                                        @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.addComment(postId, comment));
    }

    @PostMapping("/comment/upVote/{commentId}")
    public ResponseEntity<?> upVoteComment(@PathVariable int commentId, @RequestHeader("Authorization") String tokenHeader,
                                           @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.upVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Comment Up Voted");
    }

    @PostMapping("/comment/downVote/{commentId}")
    public ResponseEntity<?> downVoteComment(@PathVariable int commentId, @RequestHeader("Authorization") String tokenHeader,
                                             @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.downVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("Comment Up Voted");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId, @RequestHeader("Authorization") String tokenHeader,
                                        @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.deletePost(postId, jwtService.extractUsername(tokenHeader)));
    }

    @DeleteMapping("/delete/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @RequestHeader("Authorization") String tokenHeader,
                                        @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.deleteComment(commentId, jwtService.extractUsername(tokenHeader)));
    }

    @PostMapping("emergency/post/create")
    public ResponseEntity<?> createEmergencyPost(@RequestBody EmergencyPost emergencyPost, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        emergencyPost.setUser(userService.getUserByAuthId(userAuthDataService.getAuthId(username)));
        return ResponseEntity.status(HttpStatus.CREATED).body(emergencyPostService.createEmergencyPost(emergencyPost, username));
    }

    @DeleteMapping("emergency/post/delete/{postId}")
    public ResponseEntity<?> deleteEmergencyPost(@PathVariable int postId, @RequestHeader("Authorization") String tokenHeader,
                                                 @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(emergencyPostService.deletePost(postId, jwtService.extractUsername(tokenHeader)));
    }

    @GetMapping("/search/post")
    public ResponseEntity<?> searchPostByTitle(@RequestParam("search") String searchTitle, @RequestHeader("Authorization") String tokenHeader,
                                               @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.findPostByTitle(searchTitle));
    }
}
