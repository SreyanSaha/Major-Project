package com.help.validation;

import com.help.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.regex.Pattern;

@Component
public class UserValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9\\s]*$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,'./#\\-()&@]+$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    public String isValidUserDetails(User user) {
        if(!isValidName(user.getUserFirstName())) return "User first name is invalid.";
        if(!isValidName(user.getUserLastName())) return "User last name is invalid.";
        if(!isValidEmail(user.getUserEmailId())) return "User email is invalid.";
        if(!isValidPhone(String.valueOf(user.getUserPhoneNumber()))) return "User phone number is invalid.";
        if(!isValidAddress(user.getStreet()))return "User address is invalid.";
        if(!isValidName(user.getCity()))return "User city is invalid.";
        if(!isValidName(user.getState()))return "User state is invalid.";
        if(!isValidZipCode(user.getZipCode())) return "User zip code is invalid.";
        return "Validated.";
    }

    public boolean isValidNumeric(String numeric){
        if(numeric==null || numeric.trim().isEmpty())return false;
        return NUMERIC_PATTERN.matcher(numeric.trim()).matches();
    }

    public boolean isValidAddress(String address){
        if(address==null || address.trim().isEmpty())return false;
        return ADDRESS_PATTERN.matcher(address.trim()).matches();
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
        if(phone==null || phone.trim().isEmpty())return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
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
