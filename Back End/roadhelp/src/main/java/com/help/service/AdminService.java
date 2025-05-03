package com.help.service;

import com.help.dto.OtpForVerification;
import com.help.dto.PostStatusWrapper;
import com.help.email.EmailService;
import com.help.email.GetMailText;
import com.help.model.Admin;
import com.help.model.OtpDetails;
import com.help.model.Post;
import com.help.repository.AdminRepository;
import com.help.repository.PostRepository;
import com.help.repository.UserAuthDataRepository;
import com.help.validation.AdminValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.random.RandomGenerator;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final AdminValidation adminValidation;
    private final PostRepository postRepository;
    private final EmailService emailService;
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    @Autowired
    public AdminService(AdminRepository adminRepository, UserAuthDataRepository userAuthDataRepository,
                        AdminValidation adminValidation, PostRepository postRepository, EmailService emailService) {
        this.adminRepository = adminRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.adminValidation = adminValidation;
        this.postRepository = postRepository;
        this.emailService = emailService;
    }

    public boolean sendRegistrationEmailOTP(String email) {
        if(!adminValidation.isValidEmail(email))return false;
        OtpDetails otpDetails=new OtpDetails();
        otpStorage.put(email,otpDetails);
        return emailService.sendRegistrationEmailOTP(email,GetMailText.adminMailSubjectOTP,GetMailText.adminMailTextOTP.replace("{}", otpDetails.getOtp()));
    }

    public int verifyRegistrationOTP(OtpForVerification otpForVerification){
        if(!otpStorage.get(otpForVerification.getEmail()).getGeneratedAt().plusMinutes(1).isAfter(LocalDateTime.now())){otpStorage.remove(otpForVerification.getEmail());return -1;}
        if(otpStorage.get(otpForVerification.getEmail()).getOtp().compareTo(otpForVerification.getOtp()) != 0) return -2;
        otpStorage.remove(otpForVerification.getEmail());
        return 0;
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
