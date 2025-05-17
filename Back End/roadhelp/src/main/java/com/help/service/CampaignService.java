package com.help.service;

import com.help.model.Campaign;
import com.help.model.User;
import com.help.repository.CampaignRepository;
import com.help.repository.UserAuthDataRepository;
import com.help.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService {
    private final UserRepository userRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(UserRepository userRepository, CampaignRepository campaignRepository, UserAuthDataRepository userAuthDataRepository){
        this.userRepository=userRepository;
        this.campaignRepository=campaignRepository;
        this.userAuthDataRepository=userAuthDataRepository;
    }

    public String createCampaign(Campaign campaign, List<MultipartFile> images, MultipartFile upiQRImage, String uname){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(!uname.equals(username))return "Invalid username!";
        System.out.println("Username:"+username);
        Optional<User> user=userRepository.findByUsername(username);
        if(user.isEmpty())return "User does not exists.";
        campaign.setUser(user.get());
        campaign.setCampaignOrganizerName(user.get().getUserFirstName()+" "+user.get().getUserLastName());
        campaign.setCampaignOrganizerProfileImagePath(user.get().getProfileImagePath());
        campaign.setCampaignOrganizerEmail(user.get().getUserEmailId());
        campaign.setCampaignOrganizerContact(user.get().getUserPhoneNumber());
        String []campaignPaths=saveCampaignImages(images, upiQRImage);
        if(campaignPaths==null)return "Failed to create campaign.";
        campaign.setUpiImage(campaignPaths[0]);
        campaign.setImagePath1(campaignPaths[1]);
        campaign.setImagePath2(campaignPaths[2]);
        campaign.setImagePath3(campaignPaths[3]);
        campaign.setImagePath4(campaignPaths[4]);
        campaign.setImagePath5(campaignPaths[5]);
        campaignRepository.save(campaign);
        return "created";
    }

    private String[] saveCampaignImages(List<MultipartFile> images, MultipartFile upiQRImage){
        String []imagePaths=new String[6];int count=0;Path rootPath=Paths.get("");
        try{
            if(upiQRImage.isEmpty() || upiQRImage.getSize() > (5 * 1024 * 1024))return null;
            String campaignUpi=rootPath.toAbsolutePath().toString()+"/src/main/java/com/help/allMedia/campaignPaymentImageUPI";
            Path campaignUpiPath=Paths.get(campaignUpi);
            if(!Files.exists(campaignUpiPath))Files.createDirectories(campaignUpiPath);
            Path upiUploadPath=campaignUpiPath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+upiQRImage.getOriginalFilename());
            imagePaths[count++]=String.valueOf(upiUploadPath);
            upiQRImage.transferTo(upiUploadPath.toFile());
            for(MultipartFile image:images){
                if(image.isEmpty() || image.getSize() > (5 * 1024 * 1024))return null;
                String campaignImagePath=rootPath.toAbsolutePath().toString()+"/src/main/java/com/help/allMedia/campaignImages";
                Path campaignPath=Paths.get(campaignImagePath);
                if(!Files.exists(campaignPath))Files.createDirectories(campaignPath);
                Path uploadPath=campaignPath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+image.getOriginalFilename());
                imagePaths[count++]=uploadPath.toString();
                image.transferTo(uploadPath.toFile());
            }
            return imagePaths;
        }catch (Exception e){
            System.out.println(e.toString());
            for(String path:imagePaths) try{Files.delete(Paths.get(path));}catch (Exception e1){System.out.println(e1.toString());}
        }
        return null;
    }
}
