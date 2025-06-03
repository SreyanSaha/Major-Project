package com.help.controller;

import com.help.dto.CampaignPostData;
import com.help.dto.PostData;
import com.help.dto.ServiceResponse;
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
        ServiceResponse<PostData> response = postService.getLimitedPosts();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("campaigns")
    public ResponseEntity<?> getCampaigns(){
        ServiceResponse<List<CampaignPostData>> response = campaignService.getLimitedCampaigns();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all-posts/{startingId}")
    public ResponseEntity<?> getAllPosts(@PathVariable int startingId){
        ServiceResponse<PostData> response = postService.getAllPosts(startingId);
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getMsg());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all-campaigns/{startingId}")
    public ResponseEntity<?> getAllCampaigns(@PathVariable int startingId){
        ServiceResponse<List<CampaignPostData>> response = campaignService.getAllCampaigns(startingId);
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getMsg());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all-emergency-posts/{startingId}")
    public ResponseEntity<?> getAllEmergencyPosts(@PathVariable int startingId){
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("emergency")
    public ResponseEntity<?> getAllEmergencyPosts(){
        return ResponseEntity.ok().body(emergencyPostService.getLimitedEmergencyPosts());
    }
}
