package com.graduationproject.authorization_service.dto.response;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String avatar;
    private Boolean isActive;
}
