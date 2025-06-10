package com.help.controller;

import com.help.dto.*;
import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.model.Post;
import com.help.service.CampaignService;
import com.help.service.EmergencyPostService;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class HomeController {
    private final UserService userService;
    private final EmergencyPostService emergencyPostService;
    private final PostService postService;
    private final CampaignService campaignService;

    @Autowired
    public HomeController(UserService userService, EmergencyPostService emergencyPostService, PostService postService, CampaignService campaignService){
        this.userService = userService;
        this.emergencyPostService = emergencyPostService;
        this.postService = postService;
        this.campaignService = campaignService;
    }

    @PostMapping("token/health")
    public ResponseEntity<?> isTokenValid(){
        return ResponseEntity.status(HttpStatus.OK).body("validated.");
    }

    @GetMapping("posts")
    public ResponseEntity<?> getPosts(){
        final int page=0,size=10;
        ServiceResponse<PostData> response = postService.getLimitedPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("campaigns")
    public ResponseEntity<?> getCampaigns(){
        final int page=0,size=10;
        ServiceResponse<CampaignPostData> response = campaignService.getLimitedCampaigns(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("emergency")
    public ResponseEntity<?> getEmergency(){
        final int page=0,size=10;
        ServiceResponse<EmergencyPostData> response = emergencyPostService.getLimitedEmergencyPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable int postId){
        ServiceResponse<Optional<FullPostData>> response = postService.getPostById(postId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("campaigns/{campaignId}")
    public ResponseEntity<?> getCampaignById(@PathVariable int campaignId){
        ServiceResponse<Optional<FullCampaignData>> response = campaignService.getCampaignById(campaignId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("emergency/{emergencyPostId}")
    public ResponseEntity<?> getEmergencyPostById(@PathVariable int emergencyPostId){
        ServiceResponse<Optional<FullEmergencyPostData>> response = emergencyPostService.getEmergencyPostById(emergencyPostId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all-posts/page/{page}/size/{size}")
    public ResponseEntity<?> getAllPosts(@PathVariable int page, @PathVariable int size){
        ServiceResponse<Page<PostData>> response = postService.getAllPosts(page, size);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all-campaigns/page/{page}/size/{size}")
    public ResponseEntity<?> getAllCampaigns(@PathVariable int page, @PathVariable int size){
        ServiceResponse<Page<CampaignPostData>> response = campaignService.getAllCampaigns(page, size);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all-emergency/page/{page}/size/{size}")
    public ResponseEntity<?> getAllEmergencyPosts(@PathVariable int page, @PathVariable int size){
        ServiceResponse<Page<EmergencyPostData>> response = emergencyPostService.getAllEmergencyPosts(page, size);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
