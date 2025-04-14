package com.help.controller;

import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.jwt.service.UserAuthDataService;
import com.help.model.Post;
import com.help.model.PostComment;
import com.help.service.PostService;
import com.help.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final UserAuthDataService userAuthDataService;

    @Autowired
    public PostController(PostService postService, JwtService jwtService, CustomUserDetailsService customUserDetailsService, UserService userService, UserAuthDataService userAuthDataService) {
        this.postService = postService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        this.userAuthDataService = userAuthDataService;
    }
    @PostMapping("/createPost")
    public ResponseEntity<?> reportIssue(@RequestBody Post post, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
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
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.upVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("upVoted");
    }

    @PostMapping("/downVote/{postId}")
    public ResponseEntity<?> downVote(@PathVariable int postId, @RequestHeader("Authorization") String tokenHeader,
                                      @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.downVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("downVoted");
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> addComment(@PathVariable int postId, @RequestBody PostComment comment, @RequestHeader("Authorization") String tokenHeader,
                                        @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.addComment(postId, comment));
    }

    @PostMapping("/comment/upvVote/{commentId}")
    public ResponseEntity<?> upVoteComment(@PathVariable int commentId, @RequestHeader("Authorization") String tokenHeader,
                                           @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.upVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("commentUpVoted");
    }

    @PostMapping("/comment/downVote/{commentId}")
    public ResponseEntity<?> downVoteComment(@PathVariable int commentId, @RequestHeader("Authorization") String tokenHeader,
                                             @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.downVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(username)).getUserId());
        return ResponseEntity.ok().body("commentUpVoted");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId, @RequestHeader("Authorization") String tokenHeader,
                                        @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.deletePost(postId);
        return ResponseEntity.ok().body("PostDeleted");
    }
}
