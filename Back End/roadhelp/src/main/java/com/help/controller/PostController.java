package com.help.controller;

import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.jwt.service.JwtUtil;
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
import java.util.List;

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
    public ResponseEntity<?> reportIssue(@RequestBody Post post, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        post.setUser(userService.getUserByAuthId(userAuthDataService.getAuthId(userDetails.getUsername())));
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post));
    }

//    @GetMapping("/location/{postId}/{username}/{token}")
//    public ResponseEntity<?> getPostLocation(@PathVariable int postId, @PathVariable String username, @PathVariable String token) {
//        if(!jwtService.validateToken(token,customUserDetailsService.loadUserByUsername(username)))
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.getPostLocation(postId));
//    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyPosts(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.getNearbyPosts(lat, lon, radius));
    }

    @PostMapping("/upVote/{postId}")
    public ResponseEntity<?> upVote(@PathVariable int postId, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.upVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(userDetails.getUsername())).getUserId());
        return ResponseEntity.ok().body("upVoted");
    }

    @PostMapping("/downVote/{postId}")
    public ResponseEntity<?> downVote(@PathVariable int postId, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.downVotePost(postId, userService.getUserByAuthId(userAuthDataService.getAuthId(userDetails.getUsername())).getUserId());
        return ResponseEntity.ok().body("downVoted");
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> addComment(@PathVariable int postId, @RequestBody PostComment comment, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(postService.addComment(postId, comment));
    }

    @PostMapping("/comment/upvVote/{commentId}")
    public ResponseEntity<?> upVoteComment(@PathVariable int commentId, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.upVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(userDetails.getUsername())).getUserId());
        return ResponseEntity.ok().body("commentUpVoted");
    }

    @PostMapping("/comment/downVote/{commentId}")
    public ResponseEntity<?> downVoteComment(@PathVariable int commentId, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.downVoteComment(commentId, userService.getUserByAuthId(userAuthDataService.getAuthId(userDetails.getUsername())).getUserId());
        return ResponseEntity.ok().body("commentUpVoted");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails){
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !jwtService.validateToken(authHeader.substring(7),customUserDetailsService.loadUserByUsername(userDetails.getUsername())))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        postService.deletePost(postId);
        return ResponseEntity.ok().body("PostDeleted");
    }
}
