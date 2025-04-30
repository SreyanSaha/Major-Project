package com.help.validation;

import com.help.model.Admin;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class AdminValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{2,50}$");
    private static final Pattern AADHAR_PATTERN = Pattern.compile("^\\d{12}$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");

    public String isValidAdminDetails(Admin admin) {
        if (!isValidName(admin.getAdminFirstName())) return "Admin first name is invalid.";
        else if(!isValidName(admin.getAdminLastName())) return "Admin last name is invalid.";
        else if(!isValidEmail(admin.getAdminEmailId())) return "Admin email is invalid.";
        else if(!isValidPhone(String.valueOf(admin.getAdminPhoneNumber()))) return "Admin phone number is invalid.";
        else if(!isValidAadhar(String.valueOf(admin.getAadharCardNumber())))return "Admin aadhar number is invalid.";
        else if(!isValidZipCode(admin.getZipCode())) return "Admin zip code is invalid.";
        return "Validated.";
    }

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
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


