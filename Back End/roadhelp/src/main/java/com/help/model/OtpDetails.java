package com.help.model;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class OtpDetails {
    private static final int OTP_LENGTH=6;
    private StringBuilder otp=new StringBuilder(OTP_LENGTH);
    private LocalDateTime generatedAt;

    public OtpDetails(){
        SecureRandom rand=new SecureRandom();
        for(int i=1;i<=OTP_LENGTH;i++){
            this.otp.append(String.valueOf(rand.nextInt(10)));
        }
        this.generatedAt=LocalDateTime.now();
    }

    public StringBuilder getOtp() {
        return otp;
    }

    public void setOtp(StringBuilder otp) {
        this.otp = otp;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
