package com.help.controller;

import com.help.model.EmergencyPost;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency-post")
public class EmergencyPostController {

    @PostMapping("create/emergency/post")
    public ResponseEntity<?> createEmergencyPost(@RequestBody EmergencyPost emergencyPost, @RequestHeader("Username") String username){

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @DeleteMapping("delete/emergency/post/{postId}")
    public ResponseEntity<?> deleteEmergencyPost(@PathVariable int postId, @RequestHeader("Username") String username){
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/search/emergency/post")
    public ResponseEntity<?> searchEmergencyPost(@RequestParam("search") String search,  @RequestHeader("Username") String username){
        return ResponseEntity.ok().body("");
    }
}
