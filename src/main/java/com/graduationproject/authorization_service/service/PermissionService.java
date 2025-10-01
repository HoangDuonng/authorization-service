package com.graduationproject.authorization_service.service;

import com.graduationproject.authorization_service.dto.request.CreatePermissionRequestDTO;
import com.graduationproject.authorization_service.dto.response.PermissionResponseDTO;
import java.util.List;

public interface PermissionService {
    List<PermissionResponseDTO> getAllPermissions();

    PermissionResponseDTO getPermissionById(Integer id);

    PermissionResponseDTO createPermission(CreatePermissionRequestDTO request);

    PermissionResponseDTO updatePermission(Integer id, CreatePermissionRequestDTO request);

    void deletePermission(Integer id);
}
