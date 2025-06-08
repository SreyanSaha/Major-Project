package com.help.service;

import com.help.dto.OtpForVerification;
import com.help.dto.ServiceResponse;
import com.help.dto.UserProfile;
import com.help.dto.UserSearchData;
import com.help.email.EmailService;
import com.help.email.GetMailText;
import com.help.model.OtpDetails;
import com.help.model.User;
import com.help.repository.UserAuthDataRepository;
import com.help.repository.UserRepository;
import com.help.validation.UserValidation;
import jakarta.mail.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final UserValidation userValidation;
    private final EmailService emailService;
    private final GeoService geoService;
    private final ConcurrentHashMap<String, OtpDetails> otpStorage = new ConcurrentHashMap<String, OtpDetails>();

    @Autowired
    public UserService(UserRepository userRepository, UserAuthDataRepository userAuthDataRepository,
                       UserValidation userValidation, EmailService emailService, GeoService geoService) {
        this.userRepository = userRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.userValidation=userValidation;
        this.emailService=emailService;
        this.geoService=geoService;
    }

    public boolean sendRegistrationEmailOTP(String email){
        String updatedEmail=email.replace("\""," ").trim();
        if(!userValidation.isValidEmail(updatedEmail))return false;
        OtpDetails otpDetails=new OtpDetails();
        this.otpStorage.put(updatedEmail,otpDetails);
        return emailService.sendRegistrationEmailOTP(updatedEmail, GetMailText.userMailSubjectOTP,GetMailText.userMailTextOTP.replace("{}", otpDetails.getOtp()));
    }

    public int verifyRegistrationOTP(OtpForVerification otpForVerification){
        otpForVerification.setEmail(otpForVerification.getEmail().replace("\""," ").trim());
        if(!this.otpStorage.get(otpForVerification.getEmail()).getGeneratedAt().plusMinutes(1).isAfter(LocalDateTime.now())){this.otpStorage.remove(otpForVerification.getEmail());return -1;}
        if(this.otpStorage.get(otpForVerification.getEmail()).getOtp().compareTo(otpForVerification.getOtp()) != 0) return -2;
        this.otpStorage.remove(otpForVerification.getEmail());
        return 0;
    }

    public User getUserByAuthId(int authId){
        return userRepository.getUserByAuthData_AuthId(authId);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByAuthData_AuthId(userAuthDataRepository.findByUsername(username).get().getAuthId());
    }

    public ServiceResponse<UserProfile> updateUser(String uname, User newUser, MultipartFile image) {
        String msg=userValidation.isValidUserDetails(newUser);
        if(!msg.equals("Validated.")) {return new ServiceResponse<>(msg,null);}
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!uname.equals(username))return new ServiceResponse<>("Invalid username!");
        String root=Paths.get("").toAbsolutePath().toString();
        String profileImagePath=null;
        User user=userRepository.findByUsername(username).get();
        user.setUserFirstName(newUser.getUserFirstName());
        user.setUserLastName(newUser.getUserLastName());
        user.setUserPhoneNumber(newUser.getUserPhoneNumber());
        user.setCity(newUser.getCity());
        user.setState(newUser.getState());
        user.setStreet(newUser.getStreet());
        user.setZipCode(newUser.getZipCode());
        user.setCountry(newUser.getCountry());
        if(image!=null && !image.isEmpty()) {
            if(user.getProfileImagePath()!=null && !user.getProfileImagePath().isEmpty() && !deleteExistingProfileImage(user.getProfileImagePath()))
                return new ServiceResponse<>("Failed to update profile image.");
            profileImagePath = saveUserProfileImage(image, root);
            if(profileImagePath==null)return new ServiceResponse<>("Failed to update profile image.");
            user.setProfileImagePath(profileImagePath.replace(root+"\\allMedia",""));
        }
        userRepository.save(user);
        return new ServiceResponse<>("updated.", userRepository.findUserProfile(uname).get());
    }

    private boolean deleteExistingProfileImage(String imagePath){
        String root=Paths.get("").toAbsolutePath().toString();
        Path profilePath=Paths.get(root+"/allMedia"+imagePath);
        try{Files.delete(profilePath);return true;}catch (Exception e){e.fillInStackTrace();}
        return false;
    }

    private String saveUserProfileImage( MultipartFile image, String root){
        String path=null;
        try{
            if(image.isEmpty() || image.getSize() > (5 * 1024 * 1024))return null;
            Path postImagePath=Paths.get(root+"/allMedia/userProfileImage");
            if(!Files.exists(postImagePath))Files.createDirectories(postImagePath);
            Path uploadPath=postImagePath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+image.getOriginalFilename());
            path=uploadPath.toString();
            image.transferTo(uploadPath.toFile());
            return path;
        }catch (Exception e){
            e.printStackTrace();
            try{Files.delete(Paths.get(path));}catch (Exception e1){System.out.println(e1.toString());}
        }
        return null;
    }

    public void deleteUser(String username) {

    }

    public ServiceResponse<UserProfile> getUserById(int userId, String uname) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!uname.trim().replace("\"","").equals(username))return new ServiceResponse<>("Invalid username!", null);
        return new ServiceResponse<>("Found user.", userRepository.findUserById(userId).get());
    }

    public ServiceResponse<Page<UserSearchData>> searchUser(String searchString, int page, int size) {
        if(!userValidation.isValidName(searchString))return new ServiceResponse<>("Invalid searching.");
        PageRequest pageRequest = PageRequest.of(page, size);
        searchString = searchString.trim();
        String firstName, lastName;
        Page<UserSearchData> response=null;
        if(searchString.contains(" ")){
            firstName=searchString.substring(0, searchString.lastIndexOf(" "));
            lastName=searchString.substring(searchString.lastIndexOf(" "));
            response = userRepository.searchUser(pageRequest, firstName, lastName);
        }else response = userRepository.searchUser(pageRequest, searchString);
        return new ServiceResponse<>(response.getTotalPages()==0?"No such user found.":"", response);
    }

    public ServiceResponse<UserProfile> getUserProfile(String uname) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!uname.trim().replace("\"","").equals(username))return new ServiceResponse<>("Invalid username!", null);
        return new ServiceResponse<>("Found user.", userRepository.findUserProfile(username).get());
    }

    public ServiceResponse<Optional<UserProfile>> findOtherUsers(int userId) {
        if(!userValidation.isValidNumeric(Integer.toString(userId)))return new ServiceResponse<>("Invalid user ID.");
        Optional<UserProfile> response = userRepository.findUserProfileForSearchById(userId);
        return new ServiceResponse<>(response.isEmpty()?"No such user found.":"",response);
    }
}
