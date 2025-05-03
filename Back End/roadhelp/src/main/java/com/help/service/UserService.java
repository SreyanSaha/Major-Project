package com.help.service;

import com.help.dto.OtpForVerification;
import com.help.email.EmailService;
import com.help.email.GetMailText;
import com.help.model.OtpDetails;
import com.help.model.User;
import com.help.repository.UserAuthDataRepository;
import com.help.repository.UserRepository;
import com.help.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final UserValidation userValidation;
    private final EmailService emailService;
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<String, OtpDetails>();

    @Autowired
    public UserService(UserRepository userRepository, UserAuthDataRepository userAuthDataRepository,
                       UserValidation userValidation, EmailService emailService) {
        this.userRepository = userRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.userValidation=userValidation;
        this.emailService=emailService;
    }

    public boolean sendRegistrationEmailOTP(String email){
        if(!userValidation.isValidEmail(email))return false;
        OtpDetails otpDetails=new OtpDetails();
        this.otpStorage.put(email,otpDetails);
        System.out.println(otpStorage.get(email).getOtp());
        return emailService.sendRegistrationEmailOTP(email, GetMailText.userMailSubjectOTP,GetMailText.userMailTextOTP.replace("{}", otpDetails.getOtp()));
    }

    public int verifyRegistrationOTP(OtpForVerification otpForVerification){
        if(this.otpStorage.get(otpForVerification.getEmail())==null) System.out.println(this.otpStorage.keySet().toArray()[0]);
        if(!this.otpStorage.get(otpForVerification.getEmail().trim()).getGeneratedAt().plusMinutes(1).isAfter(LocalDateTime.now())){this.otpStorage.remove(otpForVerification.getEmail());return -1;}
        if(this.otpStorage.get(otpForVerification.getEmail().trim()).getOtp().compareTo(otpForVerification.getOtp()) != 0) return -2;
        this.otpStorage.remove(otpForVerification.getEmail().trim());
        return 0;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public User getUserByAuthId(int authId){
        return userRepository.getUserByAuthData_AuthId(authId);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByAuthData_AuthId(userAuthDataRepository.findByUsername(username).get().getAuthId());
    }

    public User updateUser(String username, User user) {
        user.setUserId(userRepository.findByUsername(username).getUserId());
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userAuthDataRepository.deleteByUsername(username);
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getUserByName(String name) {
        return userRepository.findByUserFirstNameContainingIgnoreCase(name);
    }

    public List<User> getUserByFirstNameLastName(String firstName, String lastName) {
        return userRepository.findByUserFirstNameAndUserLastNameIgnoreCase(firstName, lastName);
    }
}
