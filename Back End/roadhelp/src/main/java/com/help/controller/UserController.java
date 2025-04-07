package com.help.controller;

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

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<?> getAllUserByName(@PathVariable String name, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(userService.getUserByName(name));
    }

    @GetMapping("/user/{fname}/{lname}")
    public ResponseEntity<?> getAllUserByFirstAndLastName(@PathVariable String fname, @PathVariable String lname, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username){
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(userService.getUserByFirstNameLastName(fname, lname));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(userService.updateUser(username, user));
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String tokenHeader, @RequestHeader("Username") String username) {
        if(tokenHeader==null || !tokenHeader.startsWith("Bearer ") || !jwtService.validateToken(tokenHeader,username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        userService.deleteUser(username);
        return ResponseEntity.ok("User deleted successfully");
    }
}
