package com.help.validation;
import com.help.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostValidation {
    public String isValidPostDetails(Post post){
        return null;
    }

    private boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.matches("^[6-9]\\d{9}$");
    }
    private boolean isValidEmailId(String emailId){
        return emailId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    private boolean isValidDigits(String digits, int length) {
        String regex = "^\\d{" + length + "}$";
        return digits != null && digits.matches(regex);
    }
}
