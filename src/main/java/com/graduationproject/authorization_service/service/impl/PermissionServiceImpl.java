package com.graduationproject.authorization_service.service.impl;

import com.graduationproject.authorization_service.dto.request.CreatePermissionRequestDTO;
import com.graduationproject.authorization_service.dto.response.PermissionResponseDTO;
import com.graduationproject.authorization_service.entity.Permission;
import com.graduationproject.authorization_service.mapper.PermissionMapper;
import com.graduationproject.authorization_service.repository.PermissionRepository;
import com.graduationproject.authorization_service.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionMapper.toDtoList(permissionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponseDTO getPermissionById(Integer id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    @Transactional
    public PermissionResponseDTO createPermission(CreatePermissionRequestDTO request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new RuntimeException("Permission with this name already exists");
        }

        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDisplayName(request.getDisplayName());
        permission.setDescription(request.getDescription());
        permission.setModule(request.getModule());
        permission.setAction(request.getAction());

        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    @Override
    @Transactional
    public PermissionResponseDTO updatePermission(Integer id, CreatePermissionRequestDTO request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        if (!permission.getName().equals(request.getName()) &&
                permissionRepository.existsByName(request.getName())) {
            throw new RuntimeException("Permission with this name already exists");
        }

        permission.setName(request.getName());
        permission.setDisplayName(request.getDisplayName());
        permission.setDescription(request.getDescription());
        permission.setModule(request.getModule());
        permission.setAction(request.getAction());

        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    @Override
    @Transactional
    public void deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found");
        }
        permissionRepository.deleteById(id);
    }
}
