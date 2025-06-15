package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.*;
import com.help.model.Campaign;
import com.help.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

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
                                            @RequestPart(value = "upiQRImage", required = false)MultipartFile upiQRImage, @RequestPart("uname")String uname){
        Campaign campaign=null;
        try{campaign=new ObjectMapper().readValue(campaignJson, Campaign.class);}
        catch (Exception e){e.printStackTrace();return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create campaign.");}
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

    @PostMapping("/upVote")
    public ResponseEntity<?> upVoteCampaign(@RequestBody int campaignId){
        ServiceResponse<Optional<FullCampaignData>> response = campaignService.upVoteCampaign(campaignId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/downVote")
    public ResponseEntity<?> downVoteCampaign(@RequestBody int campaignId){
        ServiceResponse<Optional<FullCampaignData>> response = campaignService.downVoteCampaign(campaignId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editPost(@RequestBody EditCampaignData editCampaignData){
        ServiceResponse<Boolean> response = campaignService.editCampaign(editCampaignData, editCampaignData.getCampaignId());
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/data-to-edit/{campaignId}")
    public ResponseEntity<?> getCampaignDataToEdit(@PathVariable int campaignId){
        ServiceResponse<Optional<FullCampaignData>> response = campaignService.getCampaignToEdit(campaignId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportCampaign(@RequestBody int campaignId){
        ServiceResponse<Optional<FullCampaignData>> response = campaignService.reportCampaign(campaignId);
        if(response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{campaignId}")
    public ResponseEntity<?> deletePost(@PathVariable int campaignId){
        ServiceResponse<Boolean> response=campaignService.deleteCampaign(campaignId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/page/{page}/size/{size}/{searchString}")
    public ResponseEntity<?> searchCampaign(@PathVariable int page, @PathVariable int size, @PathVariable String searchString){
        ServiceResponse<Page<CampaignPostData>> response=campaignService.getSearchedCampaigns(page, size, searchString);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeCampaign(@RequestBody int campaignId){
        ServiceResponse<Boolean> response=campaignService.completeCampaigns(campaignId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
