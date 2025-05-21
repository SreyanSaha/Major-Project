package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.UserCampaign;
import com.help.jwt.service.UserAuthDataService;
import com.help.model.Campaign;
import com.help.service.CampaignService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignController {
    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService){
        this.campaignService=campaignService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCampaign(@RequestPart("campaign") String campaignJson, @RequestPart("images")List<MultipartFile> images,
                                            @RequestPart("upiQRImage")MultipartFile upiQRImage, @RequestPart("uname")String uname) throws IOException {
        System.out.println("Working campaign");
        Campaign campaign=new ObjectMapper().readValue(campaignJson,Campaign.class);
        String response=campaignService.createCampaign(campaign, images, upiQRImage, uname);
        if(response.equals("created"))return ResponseEntity.status(HttpStatus.CREATED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/all-campaigns")
    public ResponseEntity<?> getUserAllCampaigns(){
        List<UserCampaign> campaigns=campaignService.getAllCampaignsOfUser();
        if(campaigns.isEmpty())return ResponseEntity.status(HttpStatus.OK).body("No campaign pots found.");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(campaigns);
    }
}
