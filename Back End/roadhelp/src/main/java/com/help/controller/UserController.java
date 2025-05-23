package com.help.controller;

import com.help.dto.ServiceResponse;
import com.help.dto.UserProfile;
import com.help.jwt.service.CustomUserDetailsService;
import com.help.jwt.service.JwtService;
import com.help.model.User;
import com.help.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/name")// /search/name?name=search_string
    public ResponseEntity<?> getAllUserByName(@RequestParam("name") String name, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){

        return ResponseEntity.ok().body(userService.getUserByName(name));
    }

    @GetMapping("/search/fullname")// /search/fullname?fname=search_string&lname=search_string
    public ResponseEntity<?> getAllUserByFirstAndLastName(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
                                                          @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){

        return ResponseEntity.ok().body(userService.getUserByFirstNameLastName(fname, lname));
    }

    @PutMapping("/update/user")
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username) {

        return ResponseEntity.ok().body(userService.updateUser(username, user));
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username) {

        userService.deleteUser(username);
        return ResponseEntity.ok("User deleted successfully");
    }
}
