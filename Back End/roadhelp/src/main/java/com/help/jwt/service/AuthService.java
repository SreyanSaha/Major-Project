package com.help.jwt.service;

import com.help.jwt.dto.AuthResponse;
import com.help.jwt.dto.AuthRequest;
import com.help.jwt.dto.RegisterRequest;
import com.help.model.AddressDetails;
import com.help.model.Admin;
import com.help.model.User;
import com.help.model.UserAuthData;
import com.help.repository.AdminRepository;
import com.help.repository.UserAuthDataRepository;
import com.help.repository.UserRepository;
import com.help.service.GeoService;
import com.help.validation.AdminValidation;
import com.help.validation.UserValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AuthService {
    private final UserAuthDataService userAuthDataService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAuthDataRepository userAuthDataRepository;
    private final AdminValidation adminValidation;
    private final GeoService geoService;
    private final UserValidation userValidation;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public AuthService(UserAuthDataService userAuthDataService, JwtService jwtService, AuthenticationManager authenticationManager,
                       UserAuthDataRepository userAuthDataRepository, UserValidation userValidation, AdminValidation adminValidation,
                       UserRepository userRepository, GeoService geoService, AdminRepository adminRepository) {
        this.userAuthDataService = userAuthDataService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userAuthDataRepository = userAuthDataRepository;
        this.userValidation = userValidation;
        this.adminValidation = adminValidation;
        this.geoService = geoService;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public String register(RegisterRequest request, User user) {// User register
        if(userAuthDataRepository.findByUsername(request.getUsername()).isPresent()) return "User already exists.";
        UserAuthData userAuthData = new UserAuthData();
        userAuthData.setUsername(request.getUsername());
        userAuthData.setPassword(request.getPassword());
        userAuthData.setUserTypeRole((short)0);

        if(user.getStreet()==null || user.getStreet().isEmpty() || user.getCity()==null || user.getCity().isEmpty() ||
                user.getState()==null || user.getState().isEmpty() || user.getZipCode()==null || user.getZipCode().isEmpty()) {
            AddressDetails addressDetails=geoService.getAddressFromLatLng(user.getLatitude(),user.getLongitude());
            if(addressDetails==null)return "Failed to fetch the address.";
            user.setStreet(addressDetails.getStreet());
            user.setCity(addressDetails.getCity());
            user.setState(addressDetails.getState());
            user.setZipCode(addressDetails.getZip());
        }
        String response=userValidation.isValidUserDetails(user);
        if(!response.equals("Validated.")) return response;
        userAuthData.setAdmin(null);
        userAuthData.setUser(userRepository.save(user));
        userAuthDataService.saveUser(userAuthData);
        return response;
    }

    @Transactional
    public String register(RegisterRequest request, Admin admin, MultipartFile profileImage) {// Admin registration
        if(userAuthDataRepository.findByUsername(request.getUsername()).isPresent()) return "Admin already exists.";
        UserAuthData userAuthData = new UserAuthData();
        userAuthData.setUsername(request.getUsername());
        userAuthData.setPassword(request.getPassword());
        userAuthData.setUserTypeRole((short) 1);

        if(admin.getStreet()==null || admin.getStreet().isEmpty() || admin.getCity()==null || admin.getCity().isEmpty() ||
                admin.getState()==null || admin.getState().isEmpty() || admin.getZipCode()==null || admin.getZipCode().isEmpty()) {
            AddressDetails addressDetails=geoService.getAddressFromLatLng(admin.getLatitude(),admin.getLongitude());
            if(addressDetails==null)return "Failed to fetch the address.";
            admin.setStreet(addressDetails.getStreet());
            admin.setCity(addressDetails.getCity());
            admin.setState(addressDetails.getState());
            admin.setZipCode(addressDetails.getZip());
        }
        String response=adminValidation.isValidAdminDetails(admin);
        if(!response.equals("Validated.")) return response;
        String path=saveAdminProfileImage(profileImage);
        if(path==null)return "Failed to upload profile image.";
        userAuthData.setUser(null);
        admin.setProfileImagePath(path);
        userAuthData.setAdmin(adminRepository.save(admin));
        userAuthDataService.saveUser(userAuthData);
        return response;
    }

    private String saveAdminProfileImage(MultipartFile profileImage){
        try{
            if(profileImage.isEmpty() || profileImage.getSize() > (5 * 1024 * 1024))return null;
            String profileImagePath=Paths.get("").toAbsolutePath().toString()+"/allMedia/adminProfileImage";
            Path adminProfileImagePath=Paths.get(profileImagePath);
            if(!Files.exists(adminProfileImagePath))Files.createDirectories(adminProfileImagePath);
            Path uploadPath=adminProfileImagePath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+profileImage.getOriginalFilename());
            profileImage.transferTo(uploadPath.toFile());
            return uploadPath.toString();
        }catch(Exception e){e.printStackTrace();}
        return null;
    }

    public boolean canAdminLogin(AuthRequest request){
        Admin admin=adminRepository.findByUsername(request.getUsername());
        System.out.println(admin.getAdminRole() != -1 && admin.getAdminStatus() != 0);
        return admin.getAdminRole() != -1 && admin.getAdminStatus() != 0;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        AuthResponse response=null;
        if(authentication.isAuthenticated())
            response=new AuthResponse(jwtService.generateToken(request.getUsername()),request.getUsername(),(short)userAuthDataService.getUserAuthDataTypeRole(request.getUsername()));
        return response;
    }
}


/*
1. User Registration (register())
 

Accepts user details (RegisterRequest DTO).

Hashes password before saving.

Stores the user in the database.

Generates a JWT token and returns it.

2. User Login (authenticate())

Authenticates username and password.

Fetches user details from the database.

Generates a JWT token and returns it.
*/