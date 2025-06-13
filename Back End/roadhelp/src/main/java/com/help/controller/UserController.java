package com.help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.ServiceResponse;
import com.help.dto.SubscriptionDetails;
import com.help.dto.UserProfile;
import com.help.dto.UserSearchData;
import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.model.User;
import com.help.model.UserSubscriptionLog;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/subscription/details")
    public ResponseEntity<?> getSubscriptionDetails(){
        ServiceResponse<SubscriptionDetails> response=userService.getUserSubscriptionDetails();
        if(response.getObject()==null) return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/other-users/{userId}")
    public ResponseEntity<?> getOtherUser(@PathVariable int userId){
        ServiceResponse<Optional<UserProfile>> response = userService.findOtherUsers(userId);
        if(response.getObject().isEmpty())ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId, @RequestParam String uname){
        ServiceResponse<UserProfile> response=userService.getUserById(userId, uname);
        if(response.getObject()==null)ResponseEntity.status(HttpStatus.ACCEPTED).body("User not found.");
        return ResponseEntity.status(HttpStatus.OK).body(response.getObject());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String uname) {
        ServiceResponse<UserProfile> response=userService.getUserProfile(uname);
        if(response.getObject()==null)ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getMsg());
        return ResponseEntity.status(HttpStatus.OK).body(response.getObject());
    }

    @GetMapping("/search/{searchString}/page/{page}/size/{size}")
    public ResponseEntity<?> getAllUserByName(@PathVariable String searchString, @PathVariable int page, @PathVariable int size){
        ServiceResponse<Page<UserSearchData>> response = userService.searchUser(searchString, page, size);
        if(response.getObject().getTotalPages()==0)return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(@RequestPart("user") String userJson, @RequestPart("uname") String uname, @RequestPart(value = "profileImage", required = false)MultipartFile profileImage) {
        User newUser=null;

        try{newUser=new ObjectMapper().readValue(userJson, User.class);}
        catch (Exception e){e.fillInStackTrace();return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user.");}

        ServiceResponse<UserProfile> response=userService.updateUser(uname, newUser, profileImage);

        if(response.getObject()==null && !response.getMsg().equals("updated."))return ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getMsg());
        return ResponseEntity.status(HttpStatus.OK).body(response.getObject());
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportUser(@RequestBody int userId){
        ServiceResponse<Optional<UserProfile>> response = userService.reportUser(userId);
        if(response.getObject()==null || response.getObject().isEmpty())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/profile")
    public ResponseEntity<?> deleteUser() {
        ServiceResponse<Boolean> response = userService.deleteUser();
        if(!response.getObject())return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
