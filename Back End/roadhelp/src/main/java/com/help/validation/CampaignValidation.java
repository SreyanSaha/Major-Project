package com.help.validation;

import com.help.dto.EditCampaignData;
import com.help.model.Campaign;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class CampaignValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9\\s.,!?\"'()\\-]*$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,'./#\\-()&@]+$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("(?s)(?=.*[a-zA-Z])(?!\\s*$).{1,1000}");

    public String isValidCampaign(Campaign campaign) {
        if (!isValidText(campaign.getCampaignTitle())) return "Campaign title is invalid.";
        if (!isValidDescription(campaign.getCampaignDescription())) return "Campaign description is invalid.";
        if (!isValidAddress(campaign.getStreet())) return "Street is invalid.";
        if (!isValidName(campaign.getCity())) return "City is invalid.";
        if (!isValidName(campaign.getState())) return "State is invalid.";
        if (!isValidName(campaign.getCountry())) return "Country is invalid.";
        if (!isValidZip(campaign.getPostalCode())) return "Postal code is invalid.";
        return "Validated.";
    }

    public String isValidCampaign(EditCampaignData campaign) {
        if (!isValidText(campaign.getCampaignTitle())) return "Campaign title is invalid.";
        if (!isValidDescription(campaign.getCampaignDescription())) return "Campaign description is invalid.";
        if (!isValidAddress(campaign.getStreet())) return "Street is invalid.";
        if (!isValidName(campaign.getCity())) return "City is invalid.";
        if (!isValidName(campaign.getState())) return "State is invalid.";
        if (!isValidZip(campaign.getPostalCode())) return "Postal code is invalid.";
        return "Validated.";
    }

    public boolean isValidDescription(String description){
        if(description==null || description.trim().isEmpty())return false;
        return DESCRIPTION_PATTERN.matcher(description.trim()).matches();
    }

    public boolean isValidNumeric(String numeric){
        if(numeric==null || numeric.trim().isEmpty())return false;
        return NUMERIC_PATTERN.matcher(numeric.trim()).matches();
    }

    public boolean isValidAddress(String address){
        if(address==null || address.trim().isEmpty())return false;
        return ADDRESS_PATTERN.matcher(address.trim()).matches();
    }

    private boolean isValidName(String text) {
        return text != null && !text.trim().isEmpty() && NAME_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidText(String text) {
        return text != null && !text.trim().isEmpty() && ALPHANUMERIC_PATTERN.matcher(text.trim()).matches();
    }

//    private boolean isValidEmail(String email) {
//        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email.trim()).matches();
//    }
//
//    private boolean isValidPhone(String phone) {
//        return phone != null && !phone.trim().isEmpty() && PHONE_PATTERN.matcher(phone.trim()).matches();
//    }

    private boolean isValidZip(String zip) {
        return zip != null && !zip.trim().isEmpty() && ZIP_PATTERN.matcher(zip.trim()).matches();
    }
}

