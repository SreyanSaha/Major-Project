package com.help.service;

import com.help.model.EmergencyPost;
import com.help.model.User;
import com.help.repository.EmergencyPostRepository;
import com.help.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmergencyPostService {
    private final EmergencyPostRepository emergencyPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmergencyPostService(EmergencyPostRepository emergencyPostRepository, UserRepository userRepository){
        this.emergencyPostRepository = emergencyPostRepository;
        this.userRepository = userRepository;
    }
    public EmergencyPost createEmergencyPost(EmergencyPost emergencyPost, String username) {
        User user=userRepository.findByUsername(username);
        emergencyPost.setAuthorProfileName(user.getUserFirstName()+" "+user.getUserLastName());
        emergencyPost.setAuthorProfileImagePath(user.getProfileImagePath());
        return emergencyPostRepository.save(emergencyPost);
    }

    public void deletePost(int postId) {
        emergencyPostRepository.deleteById(postId);
    }
}
