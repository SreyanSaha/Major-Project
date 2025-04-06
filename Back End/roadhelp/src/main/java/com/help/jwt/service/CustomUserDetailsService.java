package com.help.jwt.service;

import com.help.model.UserAuthData;
import com.help.repository.UserAuthDataRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthDataRepository userAuthDataRepository;

    public CustomUserDetailsService(UserAuthDataRepository userRepository) {
        this.userAuthDataRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAuthData> userOptional = userAuthDataRepository.findByUsername(username);
        UserAuthData user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(user.getUserTypeRole()))
                .build();
    }
}


/*
 * Fetches user details from UserRepository.

*/
