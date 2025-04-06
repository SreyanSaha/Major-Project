package com.help.controller;

import com.help.jwt.dto.AuthRequest;
import com.help.jwt.dto.AuthResponse;
import com.help.jwt.dto.RegisterRequest;
import com.help.jwt.dto.RegisterWrapper;
import com.help.jwt.service.AuthService;
import com.help.model.Admin;
import com.help.model.User;
import com.help.service.AdminService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterWrapper wrapper) {
        if(authService.register(wrapper.getRegisterRequest())) userService.saveUser(wrapper.getUser());
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterWrapper wrapper) {
        if(authService.register(wrapper.getRegisterRequest())) adminService.saveAdmin(wrapper.getAdmin());
        return ResponseEntity.ok("Admin registered successfully!");
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
