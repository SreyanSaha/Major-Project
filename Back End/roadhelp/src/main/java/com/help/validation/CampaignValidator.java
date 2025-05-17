package com.help.validation;

import com.help.model.Campaign;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class CampaignValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9\\s.,!?\"'()\\-]*$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");

    public String isValidCampaign(Campaign campaign) {
        if (!isValidText(campaign.getCampaignTitle())) return "Campaign title is invalid.";
        if (!isValidText(campaign.getCampaignDescription())) return "Campaign description is invalid.";
        if (!isValidName(campaign.getCampaignOrganizerName())) return "Organizer name is invalid.";
        if (!isValidPhone(Long.toString(campaign.getCampaignOrganizerContact()))) return "Organizer contact is invalid.";
        if (!isValidEmail(campaign.getCampaignOrganizerEmail())) return "Organizer email is invalid.";

        if (!isValidText(campaign.getImagePath1())) return "Image path 1 is invalid.";
        if (!isValidText(campaign.getImagePath2())) return "Image path 2 is invalid.";
        if (!isValidText(campaign.getImagePath3())) return "Image path 3 is invalid.";
        if (!isValidText(campaign.getImagePath4())) return "Image path 4 is invalid.";
        if (!isValidText(campaign.getImagePath5())) return "Image path 5 is invalid.";
        if (!isValidText(campaign.getStreet())) return "Street is invalid.";
        if (!isValidName(campaign.getCity())) return "City is invalid.";
        if (!isValidName(campaign.getState())) return "State is invalid.";
        if (!isValidName(campaign.getCountry())) return "Country is invalid.";
        if (!isValidZip(campaign.getPostalCode())) return "Postal code is invalid.";

        // campaignType, status, and profileImagePath are primitive values (short), so no null check needed
        return "Validated.";
    }

    private boolean isValidName(String text) {
        return text != null && !text.trim().isEmpty() && NAME_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidText(String text) {
        return text != null && !text.trim().isEmpty() && ALPHANUMERIC_PATTERN.matcher(text.trim()).matches();
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone != null && !phone.trim().isEmpty() && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    private boolean isValidZip(String zip) {
        return zip != null && !zip.trim().isEmpty() && ZIP_PATTERN.matcher(zip.trim()).matches();
    }
}

