package com.help.validation;
import com.help.model.Post;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PostValidation {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9\\s]*$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,'./#\\-()&@]+$");

    public String isValidPostDetails(Post post){
        if(!isValidAlphanumeric(post.getPostTitle()))return "Invalid post title.";
        if(!isValidAlphanumeric(post.getPostDescription()))return "Invalid post description.";
        if(!isValidAddress(post.getStreet()))return "Post address is invalid.";
        if(!isValidName(post.getCity()))return "Post city is invalid.";
        if(!isValidName(post.getState()))return "Post state is invalid.";
        if(!isValidZipCode(post.getPostalCode())) return "Post zip code is invalid.";
        return "Validated";
    }

    public boolean isValidAddress(String address){
        if(address==null || address.trim().isEmpty())return false;
        return ADDRESS_PATTERN.matcher(address.trim()).matches();
    }

    public boolean isValidAlphanumeric(String text){
        if(text==null || text.trim().isEmpty())return false;
        return ALPHANUMERIC_PATTERN.matcher(text.trim()).matches();
    }

    public boolean isValidName(String name) {
        if(name==null || name.trim().isEmpty())return false;
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    public boolean isValidZipCode(String zip) {
        if(zip==null || zip.trim().isEmpty())return false;
        return ZIP_PATTERN.matcher(zip.trim()).matches();
    }
}
