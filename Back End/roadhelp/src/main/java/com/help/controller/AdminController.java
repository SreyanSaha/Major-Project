package com.help.controller;

import com.help.dto.FullPostData;
import com.help.dto.PostData;
import com.help.dto.PostStatusWrapper;
import com.help.dto.ServiceResponse;
import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.jwt.service.UserAuthDataService;
import com.help.service.AdminService;
import com.help.service.EmergencyPostService;
import com.help.service.PostService;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PostService postService;
    private final UserService userService;
    private final EmergencyPostService emergencyPostService;
    private final AdminService adminService;

    @Autowired
    public AdminController(PostService postService, UserService userService,
                           EmergencyPostService emergencyPostService, AdminService adminService) {
        this.postService = postService;
        this.userService = userService;
        this.emergencyPostService = emergencyPostService;
        this.adminService = adminService;
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

//    @GetMapping("/profile")
//    public ResponseEntity<?> getAdminProfile(){
//
//    }
//
//    @PostMapping("/update/post/status")
//    public ResponseEntity<?> updatePostStatus(){
//
//    }
//
//    @PostMapping("/delete/post/{postId}")
//    public ResponseEntity<?> deletePost(){
//
//    }


}
