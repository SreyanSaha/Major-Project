package com.help.jwt.service;

import com.help.jwt.dto.AuthResponse;
import com.help.jwt.dto.AuthRequest;
import com.help.jwt.dto.RegisterRequest;
import com.help.model.Admin;
import com.help.model.User;
import com.help.model.UserAuthData;
import com.help.repository.UserAuthDataRepository;
import com.help.service.AdminService;
import com.help.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserAuthDataService userAuthDataService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthDataRepository userAuthDataRepository;
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AuthService(UserAuthDataService userAuthDataService, JwtService jwtService, AuthenticationManager authenticationManager,
                       UserAuthDataRepository userAuthDataRepository, UserService userService, AdminService adminService) {
        this.userAuthDataService = userAuthDataService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userAuthDataRepository = userAuthDataRepository;
        this.userService = userService;
        this.adminService = adminService;
    }

    @Transactional
    public String register(RegisterRequest request, User user) {
        if(userAuthDataRepository.findByUsername(request.getUsername()).isPresent()) return "User already exists.";
        UserAuthData userAuthData = new UserAuthData();
        userAuthData.setUsername(request.getUsername());
        userAuthData.setPassword(request.getPassword());
        userAuthData.setUserTypeRole(request.getUserTypeRole());
        userAuthDataService.saveUser(userAuthData);
       String response=userService.saveUser(user);
       if(!response.equals("Validated."))throw new RuntimeException("Failed to register user.");
       return response;
    }

    @Transactional
    public boolean register(RegisterRequest request, Admin admin) {
        if(userAuthDataRepository.findByUsername(request.getUsername()).isPresent()) return false;
        UserAuthData userAuthData = new UserAuthData();
        userAuthData.setUsername(request.getUsername());
        userAuthData.setPassword(request.getPassword());
        userAuthData.setUserTypeRole(request.getUserTypeRole());
        userAuthDataService.saveUser(userAuthData);
        boolean response=adminService.saveAdmin(admin);
        if(!response)throw new RuntimeException("Failed to register admin.");
        return response;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        AuthResponse response=null;
        if(authentication.isAuthenticated())
            response=new AuthResponse(jwtService.generateToken(request.getUsername()),request.getUsername(),(short)userAuthDataService.getUserAuthDataTypeRole(request.getUsername()));
        return response;
    }
}


/*
1. User Registration (register())
 

Accepts user details (RegisterRequest DTO).

Hashes password before saving.

Stores the user in the database.

Generates a JWT token and returns it.

2. User Login (authenticate())

Authenticates username and password.

Fetches user details from the database.

Generates a JWT token and returns it.
*/