package com.help.jwt.service;


import com.help.model.UserAuthData;
import com.help.repository.UserAuthDataRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthDataService {

    private final UserAuthDataRepository userAuthDataRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthDataService(UserAuthDataRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userAuthDataRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuthData saveUser(UserAuthData userAuthData) {
        userAuthData.setPassword(passwordEncoder.encode(userAuthData.getPassword()));
        return userAuthDataRepository.save(userAuthData);
    }

    public int getAuthId(String username){
        return userAuthDataRepository.findByUsername(username).get().getAuthId();
    }

    public int getUserAuthDataTypeRole(String username){
        return userAuthDataRepository.findByUsername(username).get().getUserTypeRole();
    }

    public UserAuthData getUserAuthData(String username){
        return userAuthDataRepository.findByUsername(username).get();
    }
}

/*  User Registration → Saves new users to the database after encrypting passwords.*/