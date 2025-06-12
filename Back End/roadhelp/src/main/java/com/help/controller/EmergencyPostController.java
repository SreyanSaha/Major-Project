package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.CampaignPostData;
import com.help.dto.EmergencyPostData;
import com.help.dto.ServiceResponse;
import com.help.dto.UserPost;
import com.help.model.EmergencyPost;
import com.help.model.Post;
import com.help.service.EmergencyPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/emergency-post")
public class EmergencyPostController {
    private final EmergencyPostService emergencyPostService;

    @Autowired
    public EmergencyPostController(EmergencyPostService emergencyPostService){
        this.emergencyPostService=emergencyPostService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEmergencyPost(@RequestPart("emergencyPost") String emergencyPostJSON,
                                                 @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                 @RequestPart(value = "audio", required = false) MultipartFile audio,
                                                 @RequestPart("uname") String uname){
        EmergencyPost emergencyPost=null;
        try{emergencyPost=new ObjectMapper().readValue(emergencyPostJSON, EmergencyPost.class);}
        catch(Exception e){e.fillInStackTrace();return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create emergency post.");}
        String response = emergencyPostService.createEmergencyPost(images, audio, emergencyPost, uname);
        if(!response.equals("created"))return ResponseEntity.status(HttpStatus.OK).body(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/all")
    public ResponseEntity<?> getUserAllPosts(){
        ServiceResponse<EmergencyPostData> response=emergencyPostService.getAllPostsOfUser();
        if(response.getObjects().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/resolve")
    public ResponseEntity<?> resolveEmergency(@RequestBody int emergencyPostId){
        ServiceResponse<EmergencyPostData> response = emergencyPostService.resolveEmergency(emergencyPostId);
        if(response.getObject()==null)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/delete/{emergencyPostId}")
    public ResponseEntity<?> deleteEmergencyPost(@PathVariable("emergencyPostId") int emergencyPostId){
        ServiceResponse<Boolean> response = emergencyPostService.deleteEmergencyPost(emergencyPostId);
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/page/{page}/size/{size}/{searchString}")
    public ResponseEntity<?> searchEmergencyPost(@PathVariable int page, @PathVariable int size, @PathVariable String searchString){
        ServiceResponse<Page<EmergencyPostData>> response=emergencyPostService.getSearchedEmergencyPosts(page, size, searchString);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
