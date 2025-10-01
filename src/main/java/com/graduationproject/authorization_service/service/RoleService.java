package com.graduationproject.authorization_service.service;

import com.graduationproject.authorization_service.dto.request.CreateRoleRequestDTO;
import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(Long id);

    RoleResponseDTO createRole(CreateRoleRequestDTO request);

    RoleResponseDTO updateRole(Long id, CreateRoleRequestDTO request);

    void deleteRole(Long id);

    List<RoleResponseDTO> getRolesByUserId(Long userId);
}
