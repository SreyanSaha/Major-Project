package com.help.controller;

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
import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignController {
    private final CampaignService campaignService;
    private final UserService userService;
    private final UserAuthDataService userAuthDataService;

    @Autowired
    public CampaignController(CampaignService campaignService, UserService userService, UserAuthDataService userAuthDataService){
        this.campaignService=campaignService;
        this.userService=userService;
        this.userAuthDataService=userAuthDataService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCampaign(@RequestPart("campaign") Campaign campaign, List<MultipartFile> images, MultipartFile upiQRImage, String uname){
        String response=campaignService.createCampaign(campaign, images, upiQRImage, uname);
        if(response.equals("created"))return ResponseEntity.status(HttpStatus.CREATED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
