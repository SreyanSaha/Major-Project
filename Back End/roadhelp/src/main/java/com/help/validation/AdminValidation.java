package com.help.validation;

import com.help.model.Admin;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class AdminValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{5,6}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9\\s]*$");
    private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_.#/]+$");
    private static final Pattern COMPANY_NAME = Pattern.compile("^[a-zA-Z0-9&.,'()\\- ]{2,100}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,'./#\\-()&@]+$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    public String isValidAdminDetails(Admin admin) {
        if(!isValidName(admin.getAdminFirstName())) return "Admin first name is invalid.";
        if(!isValidName(admin.getAdminLastName())) return "Admin last name is invalid.";
        if(!isValidEmail(admin.getAdminEmailId())) return "Admin email is invalid.";
        if(!isValidPhone(String.valueOf(admin.getAdminPhoneNumber()))) return "Admin phone number is invalid.";
        if(!isValidAddress(admin.getStreet()))return "Admin address is invalid.";
        if(!isValidName(admin.getCity()))return "Admin city is invalid.";
        if(!isValidName(admin.getState()))return "Admin state is invalid.";
        if(!isValidZipCode(admin.getZipCode())) return "Admin zip code is invalid.";
        if(!isValidDepartment(admin.getAdminDepartment()))return "Admin department is invalid.";
        if(!isValidEmpId(admin.getAdminEmployeeId())) return "Admin employee Id is invalid.";
        if(!isValidCompanyName(admin.getAdminCompanyName()))return "Admin organization name is invalid.";
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

    public boolean isValidDepartment(String department){
       if(department==null || department.trim().isEmpty())return false;
       return NAME_PATTERN.matcher(department.trim()).matches();
    }

    public boolean isValidEmpId(String empId){
        if(empId==null || empId.trim().isEmpty()) return false;
        return EMPLOYEE_ID_PATTERN.matcher(empId.trim()).matches();
    }

    public boolean isValidCompanyName(String company){
        if(company==null || company.trim().isEmpty())return false;
        return COMPANY_NAME.matcher(company.trim()).matches();
    }

}


