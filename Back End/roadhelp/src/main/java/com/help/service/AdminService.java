package com.help.service;

import com.help.dto.*;
import com.help.email.EmailService;
import com.help.email.GetMailText;
import com.help.model.*;
import com.help.repository.*;
import com.help.validation.AdminValidation;
import com.help.validation.CampaignValidation;
import com.help.validation.PostValidation;
import com.help.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CampaignValidation campaignValidation;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    @Autowired
    public AdminService(AdminRepository adminRepository, UserAuthDataRepository userAuthDataRepository,
                        AdminValidation adminValidation, PostRepository postRepository, EmailService emailService,
                        GeoService geoService, PostValidation postValidation, CampaignValidation campaignValidation,
                        CampaignRepository campaignRepository, UserRepository userRepository, UserValidation userValidation) {
        this.adminRepository = adminRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.adminValidation = adminValidation;
        this.postRepository = postRepository;
        this.emailService = emailService;
        this.geoService = geoService;
        this.postValidation=postValidation;
        this.campaignValidation = campaignValidation;
        this.campaignRepository = campaignRepository;
        this.userRepository=userRepository;
        this.userValidation=userValidation;
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
        return adminRepository.findByUsername(username).get();
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

    @Transactional
    public ServiceResponse<Boolean> updateWorkInProgress(int postId) {
        if(!postValidation.isValidNumeric(Integer.toString(postId))) return new ServiceResponse<>("Invalid post id.", false);
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty())return new ServiceResponse<>("Post not found.", false);
        else if(post.get().getPostStatus()==(short)0) return new ServiceResponse<>("Already Work in progress.", false);
        post.get().setPostStatus((short) 0);
        postRepository.save(post.get());
        return new ServiceResponse<>("Status updated.", true);
    }

    @Transactional
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

    @Transactional
    public ServiceResponse<Boolean> approveCampaign(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Invalid CampaignId.", false);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if(campaign.isEmpty())return new ServiceResponse<>("Campaign not found.", false);
        if(campaign.get().getStatus()==(short) 0)return new ServiceResponse<>("Campaign already approved", false);
        campaign.get().setStatus((short) 0);
        campaignRepository.save(campaign.get());
        return new ServiceResponse<>("Campaign approved", true);
    }

    @Transactional
    public ServiceResponse<Boolean> rejectCampaign(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Invalid CampaignId.", false);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if(campaign.isEmpty())return new ServiceResponse<>("Campaign not found.", false);
        if(campaign.get().getStatus()==(short) -1 || campaign.get().getStatus()==(short) 1)return new ServiceResponse<>("Campaign cannot be rejected", false);
        campaign.get().setStatus((short) -1);
        campaignRepository.save(campaign.get());
        return new ServiceResponse<>("Campaign rejected", true);
    }

    public ServiceResponse<FullCampaignData> getSearchedCampaigns(String searchString) {
        List<FullCampaignData> list = campaignRepository.searchCampaignsByKeyword(searchString);
        return new ServiceResponse<>(list.isEmpty()?"No campaigns are found.":"", list);
    }

    public ServiceResponse<AdminProfile> getProfile() {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if(admin.isEmpty())return new ServiceResponse<>("No admin found.");
        return new ServiceResponse<>(adminRepository.fetchAdminById(admin.get().getAdminId()));
    }

    public ServiceResponse<UserProfile> getAllUserProfiles(){
        List<UserProfile> profileList = userRepository.findUserProfiles();
        return new ServiceResponse<>(profileList.isEmpty()?"No user profiles found.":"", profileList);
    }

    @Transactional
    public ServiceResponse<Boolean> timeOutUser(int userId) {
        if(!userValidation.isValidNumeric(Integer.toString(userId)))return new ServiceResponse<>("Invalid user id.", false);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())return new ServiceResponse<>("User not found.", false);
        if(user.get().getTimeOutEndTime()!=null && user.get().getTimeOutEndTime().isAfter(LocalDateTime.now()))return new ServiceResponse<>("User is already in time out.", false);
        user.get().setTimeOutEndTime(LocalDateTime.now().plusWeeks(1));
        user.get().setUserReports(0);
        userRepository.save(user.get());
        return new ServiceResponse<>("User in time out.", true);
    }

    @Transactional
    public ServiceResponse<Boolean> removeTimeOutUser(int userId) {
        if(!userValidation.isValidNumeric(Integer.toString(userId)))return new ServiceResponse<>("Invalid user id.", false);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())return new ServiceResponse<>("User not found.", false);
        if(user.get().getTimeOutEndTime()==null && user.get().getTimeOutEndTime().isBefore(LocalDateTime.now()))return new ServiceResponse<>("User is not in time out.", false);
        user.get().setTimeOutEndTime(null);
        userRepository.save(user.get());
        return new ServiceResponse<>("User is out from time out.", true);
    }

    public ServiceResponse<UserProfile> getSearchedUsers(String searchString) {
        List<UserProfile> userList = userRepository.searchUserProfiles(searchString);
        return new ServiceResponse<>(userList.isEmpty()?"No users found.":"", userList);
    }

    public ServiceResponse<AdminProfile> getAllAdmins() {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Admin> admin=adminRepository.findByUsername(username);
        if(admin.isEmpty())return new ServiceResponse<>("Admin not found.");
        if(admin.get().getAdminRole()!=(short) 2)return new ServiceResponse<>("You are not authorized to access these details.");
        List<AdminProfile> admins=adminRepository.fetchAllAdmins(admin.get().getAdminId());
        return new ServiceResponse<>(admins.isEmpty()?"No admins found":"",admins);
    }

    public ServiceResponse<Boolean> approveAdmin(int adminId, int adminRole) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!postValidation.isValidNumeric(Integer.toString(adminId)) || !postValidation.isValidNumeric(Integer.toString(adminRole)))
            return new ServiceResponse<>("Invalid admin id or admin role.", false);
        Optional<Admin> admin= adminRepository.findById(adminId);
        Optional<Admin> superAdmin = adminRepository.findByUsername(username);
        if(admin.isEmpty())return new ServiceResponse<>("Admin not found.", false);
        if(superAdmin.get().getAdminRole()!=(short) 2)return new ServiceResponse<>("You are not authorized to change the status.", false);
        if(superAdmin.get().getAdminId()==adminId)return new ServiceResponse<>("Cannot change own status.", false);
        if(admin.get().getAdminStatus() == 1 && (admin.get().getAdminRole() == (short) 1 || admin.get().getAdminRole() == (short) 2))return new ServiceResponse<>("Admin already active.", false);
        admin.get().setAdminRole((short) adminRole);
        admin.get().setAdminStatus((short) 1);
        adminRepository.save(admin.get());
        return new ServiceResponse<>("Admin approved.", true);
    }

    public ServiceResponse<Boolean> rejectAdmin(int adminId) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!postValidation.isValidNumeric(Integer.toString(adminId))) return new ServiceResponse<>("Invalid admin id.", false);
        Optional<Admin> admin= adminRepository.findById(adminId);
        Optional<Admin> superAdmin = adminRepository.findByUsername(username);
        if(admin.isEmpty())return new ServiceResponse<>("Admin not found.", false);
        if(superAdmin.get().getAdminRole()!=(short) 2)return new ServiceResponse<>("You are not authorized to change the status.", false);
        if(superAdmin.get().getAdminId()==adminId)return new ServiceResponse<>("Cannot change own status.", false);
        if(admin.get().getAdminStatus() == 0 && admin.get().getAdminRole() == (short) -1)return new ServiceResponse<>("Admin already rejected.", false);
        admin.get().setAdminStatus((short) 0);
        admin.get().setAdminRole((short) -1);
        adminRepository.save(admin.get());
        return new ServiceResponse<>("Admin rejected", true);
    }

    public ServiceResponse<AdminProfile> getSearchedAdmins(String searchString) {
        List<AdminProfile> list = adminRepository.searchAdmins(searchString);
        return new ServiceResponse<>(list.isEmpty()?"No admins found.":"", list);
    }
}
