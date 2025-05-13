package com.graduationproject.authorization_service.mapper;

import com.graduationproject.authorization_service.dto.response.PermissionResponseDTO;
import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;
import com.graduationproject.authorization_service.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements BaseMapper<Role, RoleResponseDTO> {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public RoleResponseDTO toDto(Role role) {
        if (role == null) {
            return null;
        }

        Set<PermissionResponseDTO> permissions = permissionMapper
                .toDtoList(role.getPermissions().stream().toList())
                .stream()
                .collect(Collectors.toSet());

        return RoleResponseDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .displayName(role.getDisplayName())
                .description(role.getDescription())
                .isSystem(role.getIsSystem())
                .permissions(permissions)
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

}
