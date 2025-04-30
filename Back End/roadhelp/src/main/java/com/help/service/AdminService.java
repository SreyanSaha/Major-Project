package com.help.service;

import com.help.dto.PostStatusWrapper;
import com.help.model.Admin;
import com.help.model.Post;
import com.help.repository.AdminRepository;
import com.help.repository.PostRepository;
import com.help.repository.UserAuthDataRepository;
import com.help.validation.AdminValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final AdminValidation adminValidation;
    private final PostRepository postRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserAuthDataRepository userAuthDataRepository,
                        AdminValidation adminValidation, PostRepository postRepository) {
        this.adminRepository = adminRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.adminValidation = adminValidation;
        this.postRepository = postRepository;
    }

    public boolean sendRegistrationEmailOTP(String email) {
        if(!adminValidation.isValidEmail(email))return false;
        return true;
    }

    @Transactional
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin getAdminProfile(String username) {
        return adminRepository.findByUsername(username);
    }

    @Transactional
    public Post updatePostStatus(PostStatusWrapper postStatusWrapper) {
        Post post=postRepository.findById(postStatusWrapper.getPostId()).get();
        post.setPostStatus(postStatusWrapper.getPostStatus());
        return postRepository.save(post);
    }

    @Transactional
    public String deletePost(int postId) {
        Post post=postRepository.findById(postId).get();
        if(post.getPostReports()>post.getUpVoteCount()){
            postRepository.deleteById(postId);
            return "Post deleted.";
        }
        return "Post cannot be deleted.";
    }
}
