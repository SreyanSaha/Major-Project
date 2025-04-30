package com.help.controller;

import com.help.jwt.dto.AuthRequest;
import com.help.jwt.dto.AuthResponse;
import com.help.jwt.dto.RegisterWrapper;
import com.help.jwt.service.AuthService;
import com.help.service.AdminService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthService authService;
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AuthenticationController(AuthService authService, UserService userService, AdminService adminService) {
        this.authService = authService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterWrapper wrapper) {
        if(authService.register(wrapper.getRegisterRequest())) {
            userService.saveUser(wrapper.getUser());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not registered");
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/admin/email/otp")
    public boolean getAdminEmailOTP(@RequestBody String email){
        return adminService.sendRegistrationEmailOTP(email);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterWrapper wrapper) {
        if(authService.register(wrapper.getRegisterRequest())) {
            adminService.saveAdmin(wrapper.getAdmin());
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not registered");
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
