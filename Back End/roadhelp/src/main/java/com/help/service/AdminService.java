package com.help.service;

import com.help.dto.OtpForVerification;
import com.help.dto.PostStatusWrapper;
import com.help.dto.ServiceResponse;
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
import com.help.validation.PostValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final AdminValidation adminValidation;
    private final PostRepository postRepository;
    private final EmailService emailService;
    private final GeoService geoService;
    private final PostValidation postValidation;
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    @Autowired
    public AdminService(AdminRepository adminRepository, UserAuthDataRepository userAuthDataRepository,
                        AdminValidation adminValidation, PostRepository postRepository, EmailService emailService,
                        GeoService geoService, PostValidation postValidation) {
        this.adminRepository = adminRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.adminValidation = adminValidation;
        this.postRepository = postRepository;
        this.emailService = emailService;
        this.geoService = geoService;
        this.postValidation=postValidation;
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

    public Admin getAdminProfile(String username) {
        return adminRepository.findByUsername(username);
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

    public ServiceResponse<Boolean> updateWorkInProgress(int postId) {
        if(!postValidation.isValidNumeric(Integer.toString(postId))) return new ServiceResponse<>("Invalid post id.", false);
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty())return new ServiceResponse<>("Post not found.", false);
        else if(post.get().getPostStatus()==(short)0) return new ServiceResponse<>("Already Work in progress.", false);
        post.get().setPostStatus((short) 0);
        postRepository.save(post.get());
        return new ServiceResponse<>("Status updated.", true);
    }

    public ServiceResponse<Boolean> updateWorkCompleted(List<MultipartFile> images, int postId) {
        if(!postValidation.isValidNumeric(Integer.toString(postId))) return new ServiceResponse<>("Invalid post id.", false);
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty())return new ServiceResponse<>("Post not found.", false);
        else if(post.get().getPostStatus()==(short)1) return new ServiceResponse<>("Already Work Completed.", false);
        String root=Paths.get("").toAbsolutePath().toString();
        String []postImagePaths=savePostAfterWorkImages(images, root);
        if(postImagePaths==null)return new ServiceResponse<>("Failed to update the post.", false);
        post.get().setAfterWorkImagePath1(postImagePaths[0].replace(root+"\\allMedia",""));
        post.get().setAfterWorkImagePath2(postImagePaths[1].replace(root+"\\allMedia",""));
        post.get().setAfterWorkImagePath3(postImagePaths[2].replace(root+"\\allMedia",""));
        post.get().setAfterWorkImagePath4(postImagePaths[3].replace(root+"\\allMedia",""));
        post.get().setAfterWorkImagePath5(postImagePaths[4].replace(root+"\\allMedia",""));
        post.get().setPostStatus((short)1);
        postRepository.save(post.get());
        return new ServiceResponse<>("Post updated.", true);
    }

    private String[] savePostAfterWorkImages(List<MultipartFile> images, String root){
        String []imagePaths=new String[5];int count=0;
        try{
            for(MultipartFile image:images){
                if(image.isEmpty() || image.getSize() > (5 * 1024 * 1024))return null;
                Path postImagePath= Paths.get(root+"/allMedia/afterWork");
                if(!Files.exists(postImagePath))Files.createDirectories(postImagePath);
                Path uploadPath=postImagePath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+image.getOriginalFilename());
                imagePaths[count++]=uploadPath.toString();
                image.transferTo(uploadPath.toFile());
            }
            return imagePaths;
        }catch (Exception e){
            e.printStackTrace();
            for(String path:imagePaths) try{Files.delete(Paths.get(path));}catch (Exception e1){System.out.println(e1.toString());}
        }
        return null;
    }

}
