package com.help.controller;

import com.help.dto.*;
import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.jwt.service.UserAuthDataService;
import com.help.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PostService postService;
    private final UserService userService;
    private final EmergencyPostService emergencyPostService;
    private final AdminService adminService;
    private final CampaignService campaignService;

    @Autowired
    public AdminController(PostService postService, UserService userService,
                           EmergencyPostService emergencyPostService, AdminService adminService, CampaignService campaignService) {
        this.postService = postService;
        this.userService = userService;
        this.emergencyPostService = emergencyPostService;
        this.adminService = adminService;
        this.campaignService=campaignService;
    }

    @GetMapping("/all-posts")
    public ResponseEntity<?> getAllPost(){
        ServiceResponse<FullPostData> response = postService.getAllNonCompletedPosts();
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update/wip")
    public ResponseEntity<?> updateWIP(@RequestBody int postId){
        ServiceResponse<Boolean> response = adminService.updateWorkInProgress(postId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/update/work-completed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> workCompleted(@RequestPart("images") List<MultipartFile> images, @RequestPart("postId")String postId){
        ServiceResponse<Boolean> response = adminService.updateWorkCompleted(images, Integer.parseInt(postId));
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/post/{searchString}")
    public ResponseEntity<?> searchPosts(@PathVariable String searchString){
        ServiceResponse<FullPostData> response=postService.getSearchedPosts(searchString);
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all/un-approved/campaigns")
    public ResponseEntity<?> getAllUnApprovedCampaigns(){
        ServiceResponse<FullCampaignData> response=campaignService.getAllUnApprovedCampaigns();
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/approve/campaign")
    public ResponseEntity<?> approveCampaign(@RequestBody int campaignId){
        ServiceResponse<Boolean> response=adminService.approveCampaign(campaignId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/delete/campaign")
    public ResponseEntity<?> deleteCampaign(@RequestBody int campaignId){
        ServiceResponse<Boolean> response=campaignService.deleteCampaign(campaignId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reject/campaign")
    public ResponseEntity<?> rejectCampaign(@RequestBody int campaignId){
        ServiceResponse<Boolean> response=adminService.rejectCampaign(campaignId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/campaigns/{searchString}")
    public ResponseEntity<?> searchCampaign(@PathVariable String searchString){
        ServiceResponse<FullCampaignData> response=adminService.getSearchedCampaigns(searchString);
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(){
        ServiceResponse<AdminProfile> response=adminService.getProfile();
        if(response.getObject()==null)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        ServiceResponse<UserProfile> response = adminService.getAllUserProfiles();
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/time-out/user")
    public ResponseEntity<?> timeOutUser(@RequestBody int userId){
        ServiceResponse<Boolean> response = adminService.timeOutUser(userId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/remove/time-out/user")
    public ResponseEntity<?> removeTimeOutUser(@RequestBody int userId){
        ServiceResponse<Boolean> response = adminService.removeTimeOutUser(userId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId){
        ServiceResponse<Boolean> response = userService.deleteUser(userId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/all-users/{searchString}")
    public ResponseEntity<?> searchAllUsers(@PathVariable String searchString){
        ServiceResponse<UserProfile> response = adminService.getSearchedUsers(searchString);
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get/all-admins")
    public ResponseEntity<?> getAllAdmins(){
        ServiceResponse<AdminProfile> response = adminService.getAllAdmins();
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveAdmin(@RequestBody AdminApproval adminApproval){
        ServiceResponse<Boolean> response = adminService.approveAdmin(adminApproval.getAdminId(), adminApproval.getAdminRole());
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reject")
    public ResponseEntity<?> getAllAdmins(@RequestBody int adminId){
        ServiceResponse<Boolean> response = adminService.rejectAdmin(adminId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteAdmin(@RequestBody int adminId){
        ServiceResponse<Boolean> response = adminService.deleteAdmin(adminId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<?> searchAllAdmins(@PathVariable String searchString){
        ServiceResponse<AdminProfile> response = adminService.getSearchedAdmins(searchString);
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId){
        ServiceResponse<Boolean> response = postService.deletePostById(postId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
