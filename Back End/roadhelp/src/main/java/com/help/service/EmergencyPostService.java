package com.help.service;

import com.help.model.EmergencyPost;
import com.help.model.User;
import com.help.repository.EmergencyPostRepository;
import com.help.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmergencyPostService {
    private final EmergencyPostRepository emergencyPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmergencyPostService(EmergencyPostRepository emergencyPostRepository, UserRepository userRepository){
        this.emergencyPostRepository = emergencyPostRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> createEmergencyPost(EmergencyPost post, String username) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            response.put("msg", "User not found with username: " + username);
            return response;
        }

        post.setUser(user);
        post.setAuthorProfileName(user.getUserFirstName() + " " + user.getUserLastName());
        post.setAuthorProfileImagePath(user.getProfileImagePath());

        EmergencyPost savedPost = emergencyPostRepository.save(post);
        response.put("post", savedPost);
        response.put("msg", "Emergency post created successfully with photo and audio");
        return response;
    }

    public boolean deletePost(int postId, String username) {
        if(emergencyPostRepository.findById(postId).get().getUser().getUserId()!=userRepository.findByUsername(username).getUserId())return false;
        emergencyPostRepository.deleteById(postId);
        return true;
    }

    public List<EmergencyPost> getAllEmergencyPosts() {
           return emergencyPostRepository.findAllEmergencyPostForHome();
    }

    public Optional<EmergencyPost> getEmergencyPostById(int id) {
        return emergencyPostRepository.findById(id);
    }

    public List<EmergencyPost> searchEmergencyPost(String search) {
        return emergencyPostRepository.searchAllEmergencyPost(search);
    }

    public List<EmergencyPost> getLimitedEmergencyPosts() {
        return emergencyPostRepository.findLimitedEmergencyPost();
    }

    public Map<String, Object> updateEmergencyPost(String username, EmergencyPost updatedPost) {
        Map<String, Object> response = new HashMap<>();
        Optional<EmergencyPost> optionalPost = emergencyPostRepository.findById(updatedPost.getEmergencyPostId());

        if (optionalPost.isEmpty()) {
            response.put("msg", "Emergency post not found with ID: " + updatedPost.getEmergencyPostId());
            return response;
        }

        EmergencyPost existingPost = optionalPost.get();

        // Update media files
        existingPost.setImagePath1(updatedPost.getImagePath1());
        existingPost.setImagePath2(updatedPost.getImagePath2());
        existingPost.setImagePath3(updatedPost.getImagePath3());
        existingPost.setImagePath4(updatedPost.getImagePath4());
        existingPost.setImagePath5(updatedPost.getImagePath5());
        existingPost.setAudioFilePath(updatedPost.getAudioFilePath());

        // Update status
        existingPost.setEmergencyPostStatus(updatedPost.getEmergencyPostStatus());

        // Update author info from username
        User user = userRepository.findByUsername(username);
        if (user != null) {
            existingPost.setUser(user);
            existingPost.setAuthorProfileName(user.getUserFirstName() + " " + user.getUserLastName());
            existingPost.setAuthorProfileImagePath(user.getProfileImagePath());
        }

        EmergencyPost savedPost = emergencyPostRepository.save(existingPost);
        response.put("post", savedPost);
        response.put("msg", "Emergency post updated successfully");
        return response;
    }
}
