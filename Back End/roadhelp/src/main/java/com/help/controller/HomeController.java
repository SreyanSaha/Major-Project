package com.help.controller;

import com.help.dto.ServiceResponse;
import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.model.Post;
import com.help.service.EmergencyPostService;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {
    private final UserService userService;
    private final EmergencyPostService emergencyPostService;
    private final PostService postService;

    @Autowired
    public HomeController(UserService userService, EmergencyPostService emergencyPostService, PostService postService){
        this.userService = userService;
        this.emergencyPostService = emergencyPostService;
        this.postService = postService;
    }

    @GetMapping("posts")
    public ResponseEntity<?> getPosts(){
        ServiceResponse<List<Post>> response = postService.getLimitedPosts();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("all-posts/{startingId}")
    public ResponseEntity<?> getAllPosts(@PathVariable int startingId){
        ServiceResponse<List<Post>> response = postService.getAllPosts(startingId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("emergency/posts")
    public ResponseEntity<?> getAllEmergencyPosts(){
        return ResponseEntity.ok().body(emergencyPostService.getLimitedEmergencyPosts());
    }
}
