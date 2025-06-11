package com.help.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.dto.PostAnalysisResult;
import com.help.dto.ServiceResponse;
import com.help.model.User;
import com.help.model.UserSubscriptionLog;
import com.help.repository.UserRepository;
import com.help.repository.UserSubscriptionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AiService {
    private final UserSubscriptionLogRepository userSubscriptionLogRepository;
    private final UserRepository userRepository;
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public AiService(UserSubscriptionLogRepository userSubscriptionLogRepository, UserRepository userRepository) {
        this.userSubscriptionLogRepository = userSubscriptionLogRepository;
        this.userRepository = userRepository;
    }

    public ServiceResponse<PostAnalysisResult> checkSubscriptionAndGenerateAiText(MultipartFile image) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isEmpty()) {
                return new ServiceResponse<>("Error: User not found.");
            }

            User user = userOpt.get();
            Optional<UserSubscriptionLog> log = userSubscriptionLogRepository.findByUserId(user.getUserId());

            if (log.isPresent() && log.get().getEndDate().isAfter(LocalDateTime.now())) {
                return new ServiceResponse<>(analyzeImageFromMultipart(image));
            } else {
                return new ServiceResponse<>("Error: Subscription expired or not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResponse<>("Error: Unable to process image.");
        }
    }

    public PostAnalysisResult analyzeImageFromMultipart(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        String prompt = """
        Analyze the image and generate a realistic not a poetic
        post title in 1 line and a meaningful description in ( 5 to 6 ) lines.
        Your analyzed output will be used to describe the situation.
        Return the result in the following format:
        <Title>^||^<Description>
        Only output this line and nothing else.
        """;

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gemma3:latest");
        request.put("prompt", prompt);
        request.put("images", List.of(base64Image));
        request.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(OLLAMA_URL, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody());
        String combinedResult = rootNode.path("response").asText();

        return extractTitleAndDescription(combinedResult);
    }

    public PostAnalysisResult extractTitleAndDescription(String resultLine) {
        String[] parts = resultLine.split("\\^\\|\\|\\^");
        String title = parts.length > 0 ? parts[0].trim() : "Untitled";
        String description = parts.length > 1 ? parts[1].trim() : "No description available.";

        return new PostAnalysisResult(title, description);
    }


}
