package com.help.service;

import com.help.dto.*;
import com.help.model.*;
import com.help.repository.CampaignLogRepository;
import com.help.repository.CampaignReportLogRepository;
import com.help.repository.CampaignRepository;
import com.help.repository.UserRepository;
import com.help.validation.CampaignValidation;
import jakarta.transaction.Transactional;
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

    @Transactional
    public String createCampaign(Campaign campaign, List<MultipartFile> images, MultipartFile upiQRImage, String uname){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();String root=Paths.get("").toAbsolutePath().toString();
        if(!uname.equals(username))return "Invalid username!";
        Optional<User> user=userRepository.findByUsername(username);
        campaign.setUser(user.get());
        campaign.setCampaignOrganizerEmail(user.get().getUserEmailId());
        campaign.setCampaignOrganizerContact(user.get().getUserPhoneNumber());
        String []campaignPaths=saveCampaignImages(images, upiQRImage, root);
        if(campaignPaths==null)return "Failed to create campaign.";
        campaign.setUpiImage(campaignPaths[0]!=null?campaignPaths[0].replace(root+"\\allMedia",""):null);
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
            if(upiQRImage!=null && !upiQRImage.isEmpty()) {
                if(upiQRImage.getSize() > (5 * 1024 * 1024))return null;
                String campaignUpi=root+"/allMedia/campaignPaymentImageUPI";
                Path campaignUpiPath=Paths.get(campaignUpi);
                if(!Files.exists(campaignUpiPath))Files.createDirectories(campaignUpiPath);
                Path upiUploadPath=campaignUpiPath.resolve(UUID.randomUUID().toString()+"_"+System.currentTimeMillis()+"_"+upiQRImage.getOriginalFilename());
                imagePaths[count++]=String.valueOf(upiUploadPath);
                upiQRImage.transferTo(upiUploadPath.toFile());
            }else imagePaths[count++]=null;
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

    public ServiceResponse<Optional<FullCampaignData>> getCampaignToEdit(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Invalid campaign id.");
        Optional<FullCampaignData> response = campaignRepository.findCampaignByIdToEdit(campaignId);
        return new ServiceResponse<>(response.isPresent() ? "" : "No campaign found.", response);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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

    @Transactional
    public ServiceResponse<Boolean> editCampaign(EditCampaignData campaignData, int campaignId) {
        String msg=campaignValidation.isValidCampaign(campaignData);boolean msg2=campaignValidation.isValidNumeric(Integer.toString(campaignId));
        if(!msg.equals("Validated.") || !msg2)return new ServiceResponse<>(msg, false);
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByUsername(username);
        Optional<Campaign> existingCampaign=campaignRepository.findById(campaignId);

        if(existingCampaign.isEmpty() || existingCampaign.get().getUser().getUserId()!=user.get().getUserId())
            return new ServiceResponse<>("Invalid user or campaign.", false);

        existingCampaign.get().setCity(campaignData.getCity());
        existingCampaign.get().setState(campaignData.getState());
        existingCampaign.get().setStreet(campaignData.getStreet());
        existingCampaign.get().setCampaignTitle(campaignData.getCampaignTitle());
        existingCampaign.get().setCampaignDescription(campaignData.getCampaignDescription());
        campaignRepository.save(existingCampaign.get());
        return new ServiceResponse<>("updated.",true);
    }

    @Transactional
    public ServiceResponse<Boolean> deleteCampaign(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Invalid campaign id.", false);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if(campaign.isEmpty())return new ServiceResponse<>("Campaign not found.",false);
        else{
            String []imagePath={
                    campaign.get().getImagePath1(),
                    campaign.get().getImagePath2(),
                    campaign.get().getImagePath3(),
                    campaign.get().getImagePath4(),
                    campaign.get().getImagePath5(),
                    campaign.get().getUpiImage()
            };
            if(!deleteCampaignImages(imagePath))return new ServiceResponse<>("Failed to delete the campaign.",false);
        }
        campaignLogRepository.deleteByCampaignId(campaignId);
        campaignReportLogRepository.deleteByCampaignId(campaignId);
        campaignRepository.deleteById(campaign.get().getCampaignId());
        return new ServiceResponse<>("Campaign deleted.", true);
    }

    private boolean deleteCampaignImages(String []imagePaths){
        String root=Paths.get("").toAbsolutePath().toString();
        for(String path:imagePaths){
            if(path==null || path.isEmpty())continue;
            try{Files.delete(Paths.get(root+"/allMedia"+path));}catch (Exception e){e.printStackTrace();return false;}
        }
        return true;
    }

    public ServiceResponse<Page<CampaignPostData>> getSearchedCampaigns(int page, int size, String searchString) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CampaignPostData> list = campaignRepository.findAllBySearchString(searchString, pageRequest);
        return new ServiceResponse<>(list.getTotalPages()==0?"No campaigns are found.":"", list);
    }

    public ServiceResponse<FullCampaignData> getAllUnApprovedCampaigns() {
        List<FullCampaignData> response = campaignRepository.findAllCampaigns();
        return new ServiceResponse<>(response.isEmpty() ? "" : "No campaigns found.", response);
    }

    @Transactional
    public ServiceResponse<Boolean> completeCampaigns(int campaignId) {
        if(!campaignValidation.isValidNumeric(Integer.toString(campaignId)))return new ServiceResponse<>("Invalid campaign Id.", false);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if(campaign.get().getStatus()!=0)return new ServiceResponse<>("Cannot change the status of the campaign.", false);
        campaign.get().setStatus((short) 1);
        campaignRepository.save(campaign.get());
        return new ServiceResponse<>("Campaign completed.", true);
    }
}
