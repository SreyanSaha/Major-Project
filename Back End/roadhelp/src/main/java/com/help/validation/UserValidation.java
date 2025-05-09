package com.help.validation;

import com.help.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.regex.Pattern;

@Component
public class UserValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{2,50}$");
    private static final Pattern AADHAR_PATTERN = Pattern.compile("^\\d{12}$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,./#'&()-]*$\n");

    public String isValidUserDetails(User user) {
        if (!isValidName(user.getUserFirstName())) return "User first name is invalid.";
        else if(!isValidName(user.getUserLastName())) return "User last name is invalid.";
        else if(!isValidEmail(user.getUserEmailId())) return "User email is invalid.";
        else if(!isValidPhone(String.valueOf(user.getUserPhoneNumber()))) return "User phone number is invalid.";
        else if(!isValidAadhar(String.valueOf(user.getAadharCardNumber())))return "User aadhar number is invalid.";
        else if(!isValidAlphanumeric(user.getStreet()))return "User address is invalid.";
        else if(!isValidName(user.getCity()))return "User city is invalid.";
        else if(!isValidName(user.getState()))return "User state is invalid.";
        else if(!isValidZipCode(user.getZipCode())) return "User zip code is invalid.";
        return "Validated.";
    }

    public boolean isValidAlphanumeric(String text){
        if(text==null || text.trim().isEmpty())return false;
        return ALPHANUMERIC_PATTERN.matcher(text.trim()).matches();
    }

    public boolean isValidEmail(String email) {
        if(email==null || email.trim().isEmpty())return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public boolean isValidAadhar(String aadhar) {
        return aadhar != null && AADHAR_PATTERN.matcher(aadhar).matches();
    }

    public boolean isValidZipCode(String zip) {
        return zip != null && ZIP_PATTERN.matcher(zip).matches();
    }
}
