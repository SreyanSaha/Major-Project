package com.help.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    public boolean sendRegistrationEmailOTP(String toEmail, String subject, String body){
        try{
            SimpleMailMessage mail=new SimpleMailMessage();
            mail.setTo(toEmail);mail.setSubject(subject);
            mail.setText(body);javaMailSender.send(mail);
            return true;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
}
