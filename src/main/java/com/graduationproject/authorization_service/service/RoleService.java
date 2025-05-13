package com.graduationproject.authorization_service.service;

import com.graduationproject.authorization_service.dto.request.CreateRoleRequestDTO;
import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO getRoleById(Integer id);
    RoleResponseDTO createRole(CreateRoleRequestDTO request);
    RoleResponseDTO updateRole(Integer id, CreateRoleRequestDTO request);
    void deleteRole(Integer id);
}
