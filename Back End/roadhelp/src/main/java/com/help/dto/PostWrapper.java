package com.help.dto;
import com.help.model.Post;

public class PostWrapper {
    private Post post;
    private TokenAuthRequest tokenAuthRequest;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public TokenAuthRequest getTokenAuthRequest() {
        return tokenAuthRequest;
    }

    public void setTokenAuthRequest(TokenAuthRequest tokenAuthRequest) {
        this.tokenAuthRequest = tokenAuthRequest;
    }
}
