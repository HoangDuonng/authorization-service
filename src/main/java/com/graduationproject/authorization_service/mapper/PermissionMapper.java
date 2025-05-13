package com.graduationproject.authorization_service.mapper;

import com.graduationproject.authorization_service.dto.response.PermissionResponseDTO;
import com.graduationproject.authorization_service.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper implements BaseMapper<Permission, PermissionResponseDTO> {

    @Override
    public PermissionResponseDTO toDto(Permission permission) {
        if (permission == null) {
            return null;
        }
        return PermissionResponseDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .displayName(permission.getDisplayName())
                .description(permission.getDescription())
                .module(permission.getModule())
                .action(permission.getAction())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }
}
