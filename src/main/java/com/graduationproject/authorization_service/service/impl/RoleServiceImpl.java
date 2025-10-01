package com.graduationproject.authorization_service.service.impl;

import com.graduationproject.authorization_service.dto.request.CreateRoleRequestDTO;
import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;
import com.graduationproject.authorization_service.entity.Permission;
import com.graduationproject.authorization_service.entity.Role;
import com.graduationproject.authorization_service.mapper.RoleMapper;
import com.graduationproject.authorization_service.repository.PermissionRepository;
import com.graduationproject.authorization_service.repository.RoleRepository;
import com.graduationproject.authorization_service.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Integer id) {
        try {
            return roleRepository.findById(id)
                    .map(roleMapper::toDto)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        } catch (Exception e) {
            log.error("Error getting role by id: {}", id, e);
            throw new RuntimeException("Failed to get role: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public RoleResponseDTO createRole(CreateRoleRequestDTO request) {
        try {
            if (roleRepository.existsByName(request.getName())) {
                throw new RuntimeException("Role with name '" + request.getName() + "' already exists");
            }

            Role newRole = new Role();
            newRole.setName(request.getName());
            newRole.setDisplayName(request.getDisplayName());
            newRole.setDescription(request.getDescription());
            newRole.setIsSystem(request.getIsSystem());

            if (request.getPermissionIds() != null) {
                Set<Permission> permissions = new HashSet<>(
                        permissionRepository.findAllById(request.getPermissionIds()));
                if (permissions.size() != request.getPermissionIds().size()) {
                    throw new RuntimeException("Some permissions were not found. Please check the permission IDs.");
                }
                newRole.setPermissions(permissions);
            }

            Role savedRole = roleRepository.save(newRole);
            log.debug("Created role: {}", savedRole.getName());
            return roleMapper.toDto(savedRole);
        } catch (Exception e) {
            log.error("Error creating role: {}", request.getName(), e);
            throw new RuntimeException("Failed to create role: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public RoleResponseDTO updateRole(Integer id, CreateRoleRequestDTO request) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

            if (!role.getName().equals(request.getName()) &&
                    roleRepository.existsByName(request.getName())) {
                throw new RuntimeException("Role with name '" + request.getName() + "' already exists");
            }

            role.setName(request.getName());
            role.setDisplayName(request.getDisplayName());
            role.setDescription(request.getDescription());
            role.setIsSystem(request.getIsSystem());

            if (request.getPermissionIds() != null) {
                Set<Permission> permissions = new HashSet<>(
                        permissionRepository.findAllById(request.getPermissionIds()));
                if (permissions.size() != request.getPermissionIds().size()) {
                    throw new RuntimeException("Some permissions were not found. Please check the permission IDs.");
                }
                role.setPermissions(permissions);
            }

            Role updatedRole = roleRepository.save(role);
            log.debug("Updated role: {}", updatedRole.getName());
            return roleMapper.toDto(updatedRole);
        } catch (Exception e) {
            log.error("Error updating role: {}", id, e);
            throw new RuntimeException("Failed to update role: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

            if (role.getIsSystem()) {
                throw new RuntimeException("Cannot delete system role");
            }

            roleRepository.deleteById(id);
            log.debug("Deleted role with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting role: {}", id, e);
            throw new RuntimeException("Failed to delete role: " + e.getMessage());
        }
    }
}
