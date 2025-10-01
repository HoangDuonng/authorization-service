package com.graduationproject.authorization_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class RoleResponseDTO {
    private Long id;
    private String name;
    private String displayName;
    private String description;
    private Boolean isSystem;
    private Set<PermissionResponseDTO> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
