package com.help.controller;

import com.help.dto.PostAnalysisResult;
import com.help.dto.ServiceResponse;
import com.help.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class AiController {
    private final AiService aiService;

    @Autowired
    public AiController(AiService aiService){
        this.aiService=aiService;
    }

    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestPart("image") MultipartFile image){
        ServiceResponse<PostAnalysisResult> response=aiService.checkSubscriptionAndGenerateAiText(image);
        if(response.getObject()==null) return ResponseEntity.status(HttpStatus.ACCEPTED).body("Failed to generate Title and Description.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
