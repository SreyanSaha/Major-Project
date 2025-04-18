package com.help.controller;

import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.service.EmergencyPostService;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    private final UserService userService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final EmergencyPostService emergencyPostService;
    private final PostService postService;

    @Autowired
    public HomeController(UserService userService, JwtService jwtService, CustomUserDetailsService customUserDetailsService, EmergencyPostService emergencyPostService, PostService postService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.emergencyPostService = emergencyPostService;
        this.postService = postService;
    }

    @GetMapping("post/emergency/posts")
    public ResponseEntity<?> getAllEmergencyPosts(@RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.status(HttpStatus.CREATED).body(emergencyPostService.getAllEmergencyPosts());
    }
}
