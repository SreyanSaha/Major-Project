package com.help.service;

import com.help.dto.OtpForVerification;
import com.help.dto.PostStatusWrapper;
import com.help.email.EmailService;
import com.help.email.GetMailText;
import com.help.model.AddressDetails;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final AdminValidation adminValidation;
    private final PostRepository postRepository;
    private final EmailService emailService;
    private final GeoService geoService;
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    @Autowired
    public AdminService(AdminRepository adminRepository, UserAuthDataRepository userAuthDataRepository,
                        AdminValidation adminValidation, PostRepository postRepository, EmailService emailService, GeoService geoService) {
        this.adminRepository = adminRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.adminValidation = adminValidation;
        this.postRepository = postRepository;
        this.emailService = emailService;
        this.geoService = geoService;
    }

    public boolean sendRegistrationEmailOTP(String email) {
        String updatedEmail=email.replace("\""," ").trim();
        if(!adminValidation.isValidEmail(updatedEmail))return false;
        OtpDetails otpDetails=new OtpDetails();
        this.otpStorage.put(updatedEmail,otpDetails);
        return emailService.sendRegistrationEmailOTP(updatedEmail,GetMailText.adminMailSubjectOTP,GetMailText.adminMailTextOTP.replace("{}", otpDetails.getOtp()));
    }

    public int verifyRegistrationOTP(OtpForVerification otpForVerification){
        otpForVerification.setEmail(otpForVerification.getEmail().replace("\""," ").trim());
        if(!this.otpStorage.get(otpForVerification.getEmail()).getGeneratedAt().plusMinutes(1).isAfter(LocalDateTime.now())){this.otpStorage.remove(otpForVerification.getEmail());return -1;}
        if(this.otpStorage.get(otpForVerification.getEmail()).getOtp().compareTo(otpForVerification.getOtp()) != 0) return -2;
        this.otpStorage.remove(otpForVerification.getEmail());
        return 0;
    }

    @Transactional
    public boolean saveAdmin(Admin admin) {
        AddressDetails addressDetails=null;
        if(admin.getStreet()==null || admin.getCity()==null || admin.getState()==null || admin.getZipCode()==null) {
            addressDetails=geoService.getAddressFromLatLng(admin.getLatitude(),admin.getLongitude());
            if(addressDetails==null)return false;
        }
        admin.setStreet(addressDetails.getStreet());
        admin.setCity(addressDetails.getCity());
        admin.setState(addressDetails.getState());
        admin.setZipCode(addressDetails.getZip());
        adminRepository.save(admin);
        return true;
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
