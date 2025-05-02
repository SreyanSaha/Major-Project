package com.help.dto;

public class OtpForVerification {
    private String email;
    private StringBuilder otp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StringBuilder getOtp() {
        return otp;
    }

    public void setOtp(StringBuilder otp) {
        this.otp = otp;
    }
}
