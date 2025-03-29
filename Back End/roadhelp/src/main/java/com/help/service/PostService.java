package com.help.service;

import com.help.model.Post;
import com.help.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
}
