package com.help.service;

import com.help.dto.*;
import com.help.model.AddressDetails;
import com.help.model.EmergencyPost;
import com.help.model.Post;
import com.help.model.User;
import com.help.repository.EmergencyPostRepository;
import com.help.repository.UserRepository;
import com.help.validation.EmergencyPostValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class EmergencyPostService {
    private final EmergencyPostRepository emergencyPostRepository;
    private final UserRepository userRepository;
    private final EmergencyPostValidation emergencyPostValidation;
    private final GeoService geoService;

    @Autowired
    public EmergencyPostService(EmergencyPostRepository emergencyPostRepository, UserRepository userRepository, EmergencyPostValidation emergencyPostValidation,
                                GeoService geoService){
        this.emergencyPostRepository = emergencyPostRepository;
        this.userRepository = userRepository;
        this.emergencyPostValidation=emergencyPostValidation;
        this.geoService = geoService;
    }

//    public List<EmergencyPost> getAllEmergencyPosts() {
//           return emergencyPostRepository.findAllEmergencyPostForHome();
//    }
//
//    public Optional<EmergencyPost> getEmergencyPostById(int id) {
//        return emergencyPostRepository.findById(id);
//    }
//
//    public List<EmergencyPost> searchEmergencyPost(String search) {
//        return emergencyPostRepository.searchAllEmergencyPost(search);
//    }

    public ServiceResponse<EmergencyPostData> getLimitedEmergencyPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("emergencyPostUploadDateTime").descending());
        List<EmergencyPostData> list = emergencyPostRepository.findLimitedEmergencyPost(pageRequest);
        return new ServiceResponse<>(list.isEmpty()?"No emergency posts are found.":"Please login to view and access all the emergency posts.",list);
    }

    @Transactional
    public String createEmergencyPost(List<MultipartFile> images, MultipartFile audio, EmergencyPost emergencyPost, String uname) {
        if(emergencyPost.getLatitude()==null || emergencyPost.getLongitude()==null) return "Invalid location.";
        AddressDetails addressDetails=geoService.getAddressFromLatLng(emergencyPost.getLatitude(), emergencyPost.getLongitude());
        emergencyPost.setStreet(addressDetails.getStreet());
        emergencyPost.setCity(addressDetails.getCity());
        emergencyPost.setState(addressDetails.getState());
        emergencyPost.setZipCode(addressDetails.getZip());

        System.out.println("Address"+addressDetails.getStreet());

        String validationMsg = emergencyPostValidation.isValidEmergencyPostDetails(emergencyPost);
        if (!"Validated".equals(validationMsg)) return validationMsg;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!uname.equals(username)) return "Invalid username!";

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return "User not found.";
        emergencyPost.setUser(user.get());

        String root = Paths.get("").toAbsolutePath().toString();
        String[] imagePaths = saveImageFiles(images, root);

        emergencyPost.setImagePath1(imagePaths!=null && imagePaths[0]!=null?imagePaths[0].replace(root + "\\allMedia", ""):null);
        emergencyPost.setImagePath2(imagePaths!=null && imagePaths[1]!=null?imagePaths[1].replace(root + "\\allMedia", ""):null);
        emergencyPost.setImagePath3(imagePaths!=null && imagePaths[2]!=null?imagePaths[2].replace(root + "\\allMedia", ""):null);
        emergencyPost.setImagePath4(imagePaths!=null && imagePaths[3]!=null?imagePaths[3].replace(root + "\\allMedia", ""):null);
        emergencyPost.setImagePath5(imagePaths!=null && imagePaths[4]!=null?imagePaths[4].replace(root + "\\allMedia", ""):null);

        String audioPath = saveAudioFile(audio, root);

        emergencyPost.setAudioFilePath(audioPath!=null?audioPath.replace(root + "\\allMedia", ""):null);

        emergencyPostRepository.save(emergencyPost);
        return "created";
    }

    private String[] saveImageFiles(List<MultipartFile> images, String root) {
        String[] imagePaths = new String[5];int count = 0;
        try {
            for (MultipartFile image : images) {
                if(image==null)continue;
                else if (image.isEmpty() || image.getSize() > (5 * 1024 * 1024)) return null;
                Path imageDir = Paths.get(root + "/allMedia/emergencyImages");
                if (!Files.exists(imageDir)) Files.createDirectories(imageDir);
                Path imagePath = imageDir.resolve(UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + "_" + image.getOriginalFilename());
                image.transferTo(imagePath.toFile());
                imagePaths[count++] = imagePath.toString();
            }
            return imagePaths;
        } catch (Exception e) {
            e.fillInStackTrace();
            for (String path : imagePaths) {
                try { if (path != null) Files.deleteIfExists(Paths.get(path)); } catch (Exception ignored) {}
            }
        }
        return null;
    }

    private String saveAudioFile(MultipartFile audio, String root) {
        Path audioPath=null;
        try {
            if(audio == null)return null;
            if (audio.isEmpty() || audio.getSize() > (10 * 1024 * 1024)) return null;
            Path audioDir = Paths.get(root + "/allMedia/emergencyAudio");
            if (!Files.exists(audioDir)) Files.createDirectories(audioDir);
            audioPath = audioDir.resolve(UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + "_" + audio.getOriginalFilename());
            audio.transferTo(audioPath.toFile());
            return audioPath.toString();
        } catch (Exception e) {
            e.fillInStackTrace();
            try { if (audioPath != null) Files.deleteIfExists(Paths.get(audioPath.toString())); } catch (Exception ignored) {}
        }
        return null;
    }

    public ServiceResponse<Page<EmergencyPostData>> getAllEmergencyPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("emergencyPostUploadDateTime").descending());
        Page<EmergencyPostData> list = emergencyPostRepository.findAllByOrderByEmergencyPostUploadDateTimeDesc(pageRequest);
        return new ServiceResponse<>(list.getTotalPages()==0?"No additional emergency posts are found.":"", list);
    }

//    public ServiceResponse<EmergencyPost> getEmergencyPostById(int id) {
//        Optional<EmergencyPost> post = emergencyPostRepository.findById(id);
//        return post.map(value -> new ServiceResponse<>("", List.of(value)))
//                .orElseGet(() -> new ServiceResponse<>("Emergency post not found.", new ArrayList<>()));
//    }

//    public ServiceResponse<EmergencyPost> searchEmergencyPost(String search) {
//        List<EmergencyPost> posts = emergencyPostRepository.searchAllEmergencyPost(search);
//        return new ServiceResponse<>(posts.isEmpty() ? "No emergency posts found for search: " + search : "", posts);
//    }

    public ServiceResponse<EmergencyPostData> getAllPostsOfUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EmergencyPostData> list = emergencyPostRepository.findAllPostsOfUser(username);
        return new ServiceResponse<>(list.isEmpty()?"No emergency posts are found.":"",list);
    }

    @Transactional
    public ServiceResponse<EmergencyPostData> resolveEmergency(int emergencyPostId) {
        if(!emergencyPostValidation.isValidNumeric(Integer.toString(emergencyPostId)))return new ServiceResponse<>("Invalid emergency post Id.");
        Optional<EmergencyPost> emergencyPost = emergencyPostRepository.findById(emergencyPostId);
        if(emergencyPost.isEmpty())return new ServiceResponse<>("Emergency post not found.");
        emergencyPost.get().setEmergencyPostStatus((short) 1);
        emergencyPostRepository.save(emergencyPost.get());
        return new ServiceResponse<>("Emergency resolved.", emergencyPostRepository.findEmergencyPostById(emergencyPostId).get());
    }

    @Transactional
    public ServiceResponse<Boolean> deleteEmergencyPost(int emergencyPostId) {
        if(!emergencyPostValidation.isValidNumeric(Integer.toString(emergencyPostId)))return new ServiceResponse<>("Invalid emergency post Id.", false);
        Optional<EmergencyPost> emergencyPost = emergencyPostRepository.findById(emergencyPostId);
        if(emergencyPost.isEmpty())return new ServiceResponse<>("Emergency post not found.", false);
        if(!removeEmergencyPostImages(emergencyPost.get()) || !removeEmergencyPostAudio(emergencyPost.get())) return new ServiceResponse<>("Failed to delete emergency post.", false);
        emergencyPostRepository.deleteById(emergencyPostId);
        return new ServiceResponse<>("Emergency post deleted.", false);
    }

    private Boolean removeEmergencyPostImages(EmergencyPost emergencyPost){
        String []images={
                emergencyPost.getImagePath1(),
                emergencyPost.getImagePath2(),
                emergencyPost.getImagePath3(),
                emergencyPost.getImagePath4(),
                emergencyPost.getImagePath5(),
        };
        String root=Paths.get("").toAbsolutePath().toString();
        for(String image:images){
            if(image!=null && !image.isEmpty())
                try{
                    Path path=Paths.get(root+"/allMedia"+image);
                    Files.delete(path);
                }catch (Exception e){e.fillInStackTrace();return false;}
        }
        return true;
    }
    private Boolean removeEmergencyPostAudio(EmergencyPost emergencyPost){
        String root=Paths.get("").toAbsolutePath().toString();
        if(emergencyPost.getAudioFilePath()!=null && !emergencyPost.getAudioFilePath().isEmpty())
            try{
                Path path=Paths.get(root+"/allMedia"+emergencyPost.getAudioFilePath());
                Files.delete(path);
            }catch (Exception e){e.fillInStackTrace(); return false;}
        return true;
    }

    public ServiceResponse<Optional<FullEmergencyPostData>> getEmergencyPostById(int emergencyPostId) {
        if(!emergencyPostValidation.isValidNumeric(Integer.toString(emergencyPostId)))return new ServiceResponse<>("Invalid emergency post id.");
        Optional<FullEmergencyPostData> response = emergencyPostRepository.getFullEmergencyPostDataById(emergencyPostId);
        return new ServiceResponse<>(response.isPresent() ? "" : "No emergency post found.", response);
    }

    public ServiceResponse<Page<EmergencyPostData>> getSearchedEmergencyPosts(int page, int size, String searchString) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmergencyPostData> list = emergencyPostRepository.findAllBySearchString(searchString, pageRequest);
        return new ServiceResponse<>(list.getTotalPages()==0?"No emergency posts are found.":"", list);
    }

}
