package com.graduationproject.authorization_service.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponseDTO {
    private Integer id;
    private String name;
    private String displayName;
    private String description;
    private String module;
    private String action;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
