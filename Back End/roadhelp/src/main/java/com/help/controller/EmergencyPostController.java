package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.model.EmergencyPost;
import com.help.model.Post;
import com.help.service.EmergencyPostService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("delete/{postId}")
    public ResponseEntity<?> deleteEmergencyPost(@PathVariable int postId, @RequestHeader("Username") String username){
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEmergencyPost(@RequestParam("search") String search,  @RequestHeader("Username") String username){
        return ResponseEntity.ok().body("");
    }
}
