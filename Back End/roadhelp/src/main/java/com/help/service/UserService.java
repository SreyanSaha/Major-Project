package com.help.service;

import com.help.model.User;
import com.help.repository.UserAuthDataRepository;
import com.help.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthDataRepository userAuthDataRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserAuthDataRepository userAuthDataRepository) {
        this.userRepository = userRepository;
        this.userAuthDataRepository = userAuthDataRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public User getUserByAuthId(int authId){
        return userRepository.getUserByAuthData_AuthId(authId);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByAuthData_AuthId(userAuthDataRepository.findByUsername(username).get().getAuthId());
    }

    public User updateUser(String username, User user) {
        user.setUserId(userRepository.findByUsername(username).getUserId());
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userAuthDataRepository.deleteByUsername(username);
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getUserByName(String name) {
        return userRepository.findByUserFirstNameContainingIgnoreCase(name);
    }

    public List<User> getUserByFirstNameLastName(String firstName, String lastName) {
        return userRepository.findByUserFirstNameAndUserLastNameIgnoreCase(firstName, lastName);
    }
}
