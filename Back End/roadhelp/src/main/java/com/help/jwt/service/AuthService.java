package com.help.jwt.service;

import com.help.jwt.dto.AuthResponse;
import com.help.jwt.dto.AuthRequest;
import com.help.jwt.dto.RegisterRequest;
import com.help.model.UserAuthData;
import com.help.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserAuthDataService userAuthDataService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserAuthDataService userAuthDataService, JwtService jwtService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.userAuthDataService = userAuthDataService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public boolean register(RegisterRequest request) {
        UserAuthData userAuthData = new UserAuthData();
        userAuthData.setUsername(request.getUsername());
        userAuthData.setPassword(request.getPassword());
        userAuthData.setUserTypeRole(request.getUserTypeRole());
        userAuthDataService.saveUser(userAuthData);
        return true;
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtService.generateToken(request.getUsername());
        return new AuthResponse(token,request.getUsername());
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