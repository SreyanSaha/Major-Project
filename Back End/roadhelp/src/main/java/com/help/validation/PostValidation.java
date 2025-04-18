package com.help.validation;
import com.help.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostValidation {
    public String isValidPostDetails(Post post){
        if(!isValidAlphabetes(post.getPostTitle()))return "Invalid post title.";
        else if(!isValidLatitude(post.getLatitude()))return "Invalid post latitude.";
        else if(!isValidLongitude(post.getLongitude()))return "Invalid post longitude.";
        else if(!isValidStreetAddress(post.getStreet()))return "Invalid post street address.";
        else if(!isValidAlphabetes(post.getCity()))return "Invalid post city.";
        else if(!isValidAlphabetes(post.getState()))return "Invalid post state.";
        else if(!isValidAlphabetes(post.getCountry()))return "Invalid post country.";
        else if(!isValidDigits(post.getPostalCode(), 6))return "Invalid post postal code.";
        return "Validated";
    }
    public String isValidId(int id){
        if(!isValidNumber(id))return "Invalid post id.";
        return "Validated";
    }
    public String isValidTitle(String title){
        if(!isValidAlphabetes(title))return "Invalid post title.";
        return "Validated";
    }
    private boolean isValidNumber(Integer number){
        return number!=null && String.valueOf(number).matches("^\\d+$");
    }
    private boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber!=null && phoneNumber.matches("^[6-9]\\d{9}$");
    }
    private boolean isValidEmailId(String emailId){
        return emailId!=null && emailId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    private boolean isValidDigits(String digits, int length) {
        return digits != null && digits.matches("^\\d{" + length + "}$");
    }
    private boolean isValidAlphabetes(String text){
        return text!=null && !text.isBlank() && text.matches("^[a-zA-Z]+$");
    }
    private boolean isValidLatitude(Double latitude){
        return latitude!=null && String.valueOf(latitude).matches("^(-?[1-8]?[0-9](\\.\\d+)?|90(\\.0+)?)$\n");
    }
    private boolean isValidLongitude(Double longitude){
        return longitude!=null && String.valueOf(longitude).matches("^(-?(1[0-7][0-9]|[1-9]?[0-9])(\\.\\d+)?|180(\\.0+)?)$\n");
    }
    private boolean isValidStreetAddress(String street){
        return street!=null && !street.isBlank() && street.matches("^[\\p{L}\\p{N}.,#\\-/\\s]+$");
    }
}
