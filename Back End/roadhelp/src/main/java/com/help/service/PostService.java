package com.help.service;

import com.help.repository.PostCommentLogRepository;
import com.help.repository.PostCommentRepository;
import com.help.repository.PostLogRepository;
import com.help.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostLogRepository postLogRepository;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    PostCommentLogRepository postCommentLogRepository;


}
