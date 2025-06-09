package com.help.service;

import com.help.dto.CampaignPostData;
import com.help.dto.FullCampaignData;
import com.help.dto.ServiceResponse;
import com.help.dto.UserCampaign;
import com.help.model.Campaign;
import com.help.model.CampaignLog;
import com.help.model.CampaignReportLog;
import com.help.model.User;
import com.help.repository.CampaignLogRepository;
import com.help.repository.CampaignReportLogRepository;
import com.help.repository.CampaignRepository;
import com.help.repository.UserRepository;
import com.help.validation.CampaignValidation;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService {
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignValidation campaignValidation;
    private final CampaignLogRepository campaignLogRepository;
    private final CampaignReportLogRepository campaignReportLogRepository;

    @Autowired
    public CampaignService(UserRepository userRepository, CampaignRepository campaignRepository, CampaignValidation campaignValidation,
                           CampaignLogRepository campaignLogRepository, CampaignReportLogRepository campaignReportLogRepository){
        this.userRepository=userRepository;
        this.campaignRepository=campaignRepository;
        this.campaignValidation=campaignValidation;
        this.campaignLogRepository=campaignLogRepository;
        this.campaignReportLogRepository=campaignReportLogRepository;
    }

    public String createCampaign(Campaign campaign, List<MultipartFile> images, MultipartFile upiQRImage, String uname){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();String root=Paths.get("").toAbsolutePath().toString();
        if(!uname.equals(username))return "Invalid username!";
        System.out.println("Username:"+username);
        Optional<User> user=userRepository.findByUsername(username);
        campaign.setUser(user.get());
        campaign.setCampaignOrganizerEmail(user.get().getUserEmailId());
        campaign.setCampaignOrganizerContact(user.get().getUserPhoneNumber());
        String []campaignPaths=saveCampaignImages(images, upiQRImage, root);
        if(campaignPaths==null)return "Failed to create campaign.";
        campaign.setUpiImage(campaignPaths[0].replace(root+"\\allMedia",""));
        campaign.setImagePath1(campaignPaths[1].replace(root+"\\allMedia",""));
        campaign.setImagePath2(campaignPaths[2].replace(root+"\\allMedia",""));
        campaign.setImagePath3(campaignPaths[3].replace(root+"\\allMedia",""));
        campaign.setImagePath4(campaignPaths[4].replace(root+"\\allMedia",""));
        campaign.setImagePath5(campaignPaths[5].replace(root+"\\allMedia",""));
        campaignRepository.save(campaign);
        return "created";
    }

    private String[] saveCampaignImages(List<MultipartFile> images, MultipartFile upiQRImage, String root){
        String []imagePaths=new String[6];int count=0;
        try{
            if(upiQRImage.isEmpty() || upiQRImage.getSize() > (5 * 1024 * 1024))return null;
            String campaignUpi=root+"/allMedia/campaignPaymentImageUPI";
            Path campaignUpiPath=Paths.get(campaignUpi);
            if(!Files.exists(campaignUpiPath))Files.createDirectories(campaignUpiPath);
            Path upiUploadPath=campaignUpiPath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+upiQRImage.getOriginalFilename());
            imagePaths[count++]=String.valueOf(upiUploadPath);
            upiQRImage.transferTo(upiUploadPath.toFile());
            for(MultipartFile image:images){
                if(image.isEmpty() || image.getSize() > (5 * 1024 * 1024))return null;
                String campaignImagePath=root+"/allMedia/campaignImages";
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

    public List<UserCampaign> getAllCampaignsOfUser() {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return campaignRepository.findAllCampaignsOfUser(username);
    }

    public ServiceResponse<CampaignPostData> getLimitedCampaigns(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("campaignCreationTime").descending());
        List<CampaignPostData> list = campaignRepository.findLimitedCampaigns(pageRequest);
        return new ServiceResponse<CampaignPostData>(list.isEmpty()?"No campaigns are found.":"Please login to view and access all the campaigns.",list);
    }

    public ServiceResponse<Page<CampaignPostData>> getAllCampaigns(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("campaignCreationTime").descending());
        Page<CampaignPostData> list = campaignRepository.findAllCampaigns(pageRequest);
        return new ServiceResponse<Page<CampaignPostData>>(list.getTotalPages()==0?"No additional campaigns are found.":"",list);
    }

    public ServiceResponse<Optional<FullCampaignData>> getCampaignById(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Invalid campaign id.");
        Optional<FullCampaignData> response = campaignRepository.findCampaignById(campaignId);
        return new ServiceResponse<>(response.isPresent() ? "" : "No campaign found.", response);
    }

    public ServiceResponse<Optional<FullCampaignData>> upVoteCampaign(int campaignId){
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Failed to up vote the campaign.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        Optional<CampaignLog> campaignLog = campaignLogRepository.findByUserIdAndCampaignId(user.getUserId(), campaignId);
        if(campaign.isEmpty())return new ServiceResponse<>("Campaign not found.");
        if(campaignLog.isPresent()){
            if(campaignLog.get().getLog()==(short)1){// removing upvote
                campaign.get().setUpVoteCount(campaign.get().getUpVoteCount()-1);
                campaignLogRepository.deleteById(campaignLog.get().getCampaignLogId());
                campaignRepository.save(campaign.get());
                return new ServiceResponse<>("Vote removed.",campaignRepository.findCampaignById(campaignId));
            }
            else if(campaignLog.get().getLog()==(short)0){// removing the downVote and adding upVote
                campaign.get().setDownVoteCount(campaign.get().getDownVoteCount()-1);
                campaign.get().setUpVoteCount(campaign.get().getUpVoteCount()+1);
                campaignLog.get().setLog((short) 1);
                campaignLogRepository.save(campaignLog.get());
                campaignRepository.save(campaign.get());
            }
        }else{
            CampaignLog log=new CampaignLog(user.getUserId(), campaignId, (short) 1);
            campaign.get().setUpVoteCount(campaign.get().getUpVoteCount()+1);
            campaignLogRepository.save(log);
            campaignRepository.save(campaign.get());
        }
        return new ServiceResponse<>("Campaign up voted.",campaignRepository.findCampaignById(campaignId));
    }

    public ServiceResponse<Optional<FullCampaignData>> downVoteCampaign(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Failed to down vote the campaign.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        Optional<CampaignLog> campaignLog = campaignLogRepository.findByUserIdAndCampaignId(user.getUserId(), campaignId);
        if(campaign.isEmpty())return new ServiceResponse<>("Campaign not found.");
        if(campaignLog.isPresent()){
            if(campaignLog.get().getLog()==(short)1){// removing upvote adding downVote
                campaign.get().setUpVoteCount(campaign.get().getUpVoteCount()-1);
                campaign.get().setDownVoteCount(campaign.get().getDownVoteCount()+1);
                campaignLog.get().setLog((short) 0);
                campaignLogRepository.save(campaignLog.get());
                campaignRepository.save(campaign.get());
            }
            else if(campaignLog.get().getLog()==(short)0){// removing the downVote
                campaign.get().setDownVoteCount(campaign.get().getDownVoteCount()-1);
                campaignLogRepository.deleteById(campaignLog.get().getCampaignLogId());
                campaignRepository.save(campaign.get());
                return new ServiceResponse<>("Vote removed.",campaignRepository.findCampaignById(campaignId));
            }
        }else{
            CampaignLog log=new CampaignLog(user.getUserId(), campaignId, (short) 0);
            campaign.get().setDownVoteCount(campaign.get().getDownVoteCount()+1);
            campaignLogRepository.save(log);
            campaignRepository.save(campaign.get());
        }
        return new ServiceResponse<>("Campaign down voted.",campaignRepository.findCampaignById(campaignId));
    }

    public ServiceResponse<Optional<FullCampaignData>> reportCampaign(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Failed to report the campaign.");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        Optional<CampaignReportLog> campaignReportLog = campaignReportLogRepository.findByUserIdAndCampaignId(user.getUserId(), campaignId);
        if(campaign.isEmpty())return new ServiceResponse<>("Campaign not found.");
        if(campaignReportLog.isPresent()){
            campaign.get().setCampaignReports(campaign.get().getCampaignReports()-1);
            campaignReportLogRepository.deleteById(campaignReportLog.get().getCampaignReportLogId());
            campaignRepository.save(campaign.get());
            return new ServiceResponse<>("Campaign report removed.", campaignRepository.findCampaignById(campaignId));
        }else{
            CampaignReportLog log=new CampaignReportLog(user.getUserId(), campaign.get().getCampaignId(), (short) 1);
            campaign.get().setCampaignReports(campaign.get().getCampaignReports()+1);
            campaignReportLogRepository.save(log);
            campaignRepository.save(campaign.get());
        }
        return new ServiceResponse<>("Campaign reported.",campaignRepository.findCampaignById(campaignId));
    }
}
