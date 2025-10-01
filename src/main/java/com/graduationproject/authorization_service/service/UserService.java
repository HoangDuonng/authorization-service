package com.graduationproject.authorization_service.service;

import com.graduationproject.authorization_service.dto.response.UserInfoDTO;

public interface UserService {
    UserInfoDTO getUserById(Long userId);
}
