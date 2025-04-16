package com.help.controller;

import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.jwt.service.UserAuthDataService;
import com.help.service.AdminService;
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
@RequestMapping("/admin")
public class AdminController {
    private final PostService postService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final UserAuthDataService userAuthDataService;
    private final EmergencyPostService emergencyPostService;
    private final AdminService adminService;

    @Autowired
    public AdminController(PostService postService, JwtService jwtService, CustomUserDetailsService customUserDetailsService,
                          UserService userService, UserAuthDataService userAuthDataService, EmergencyPostService emergencyPostService, AdminService adminService) {
        this.postService = postService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        this.userAuthDataService = userAuthDataService;
        this.emergencyPostService = emergencyPostService;
        this.adminService = adminService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(@RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader.substring(7),customUserDetailsService.loadUserByUsername(username)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(adminService.getAdminProfile(username));
    }
}
