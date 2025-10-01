package com.graduationproject.authorization_service.service.impl;

import com.graduationproject.authorization_service.dto.response.UserInfoDTO;
import com.graduationproject.authorization_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${user-service.service-token}")
    private String serviceToken;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserInfoDTO getUserById(Long userId) {
        try {
            // Gọi API public với service token
            String url = userServiceUrl + "/api/users/" + userId;
            log.debug("Calling user service with token: {}", url);

            // Tạo headers với service token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + serviceToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("User service response status: {}, body: {}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                log.info("User service JSON response for userId {}: {}", userId, response.getBody());

                // Thử các field khác nhau có thể chứa username
                String username = null;
                if (jsonNode.has("username")) {
                    username = jsonNode.get("username").asText();
                    log.info("Found username field: {}", username);
                } else if (jsonNode.has("userName")) {
                    username = jsonNode.get("userName").asText();
                    log.info("Found userName field: {}", username);
                } else if (jsonNode.has("name")) {
                    username = jsonNode.get("name").asText();
                    log.info("Found name field: {}", username);
                } else if (jsonNode.has("fullName")) {
                    username = jsonNode.get("fullName").asText();
                    log.info("Found fullName field: {}", username);
                } else if (jsonNode.has("email")) {
                    username = jsonNode.get("email").asText();
                    log.info("Found email field: {}", username);
                }

                if (username != null && !username.isEmpty()) {
                    UserInfoDTO userInfo = new UserInfoDTO();
                    userInfo.setId(userId);
                    userInfo.setUsername(username);
                    log.info("Successfully extracted username: {} for userId: {}", username, userId);
                    return userInfo;
                } else {
                    log.warn("No valid username found in response for userId: {}", userId);
                }
            }

            log.warn("Could not extract username from user service response for userId: {}", userId);
            UserInfoDTO defaultUser = new UserInfoDTO();
            defaultUser.setId(userId);
            defaultUser.setUsername("User-" + userId);
            return defaultUser;

        } catch (Exception e) {
            log.error("Error calling user service for userId: {} - Error: {}", userId, e.getMessage(), e);
            // Trả về user info mặc định nếu không gọi được API
            UserInfoDTO defaultUser = new UserInfoDTO();
            defaultUser.setId(userId);
            defaultUser.setUsername("User-" + userId);
            return defaultUser;
        }
    }
}
