package com.help.service;

import com.help.model.User;
import com.help.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public User getUserByAuthId(int authId){
        return userRepository.getUserByAuthData_AuthId(authId);
    }
}
