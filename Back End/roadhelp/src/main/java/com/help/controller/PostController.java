package com.help.controller;

import com.help.model.Post;
import com.help.model.PostComment;
import com.help.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/createPost")
    public Post reportIssue(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/location/{postId}")
    public String getPostLocation(@PathVariable int postId) {
        return postService.getPostLocation(postId);
    }

    @GetMapping("/nearby")
    public List<Post> getNearbyPosts(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        return postService.getNearbyPosts(lat, lon, radius);
    }

    @PostMapping("/upVote/{postId}/{userID}")
    public void upVote(@PathVariable int postId, @PathVariable int userId) {
        postService.upVotePost(postId, userId);
    }

    @PostMapping("/downVote/{postId}/{userId}")
    public void downVote(@PathVariable int postId, @PathVariable int userId) {
        postService.downVotePost(postId, userId);
    }

    @PostMapping("/comment/{postId}")
    public PostComment addComment(@PathVariable int postId, @RequestBody PostComment comment) {
        return postService.addComment(postId, comment);
    }

    @PostMapping("/comment/upvVote/{commentId}/{userId}")
    public void upVoteComment(@PathVariable int commentId, @PathVariable int userId) {
        postService.upVoteComment(commentId, userId);
    }

    @PostMapping("/comment/downVote/{commentId}/{userId}")
    public void downVoteComment(@PathVariable int commentId, @PathVariable int userId) {
        postService.downVoteComment(commentId, userId);
    }

    @DeleteMapping("/delete/{postId}")
    public void deletePost(@PathVariable int postId){
        postService.deletePost(postId);
    }
}
