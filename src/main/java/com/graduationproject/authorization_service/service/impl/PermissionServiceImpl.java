package com.graduationproject.authorization_service.service.impl;

import com.graduationproject.authorization_service.dto.request.CreatePermissionRequestDTO;
import com.graduationproject.authorization_service.dto.response.PermissionResponseDTO;
import com.graduationproject.authorization_service.entity.Permission;
import com.graduationproject.authorization_service.entity.Role;
import com.graduationproject.authorization_service.mapper.PermissionMapper;
import com.graduationproject.authorization_service.repository.PermissionRepository;
import com.graduationproject.authorization_service.repository.RoleRepository;
import com.graduationproject.authorization_service.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionMapper.toDtoList(permissionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponseDTO getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));
    }

    @Override
    @Transactional
    public PermissionResponseDTO createPermission(CreatePermissionRequestDTO request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new RuntimeException("Quyền với tên này đã tồn tại");
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
    public PermissionResponseDTO updatePermission(Long id, CreatePermissionRequestDTO request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));

        if (!permission.getName().equals(request.getName()) &&
                permissionRepository.existsByName(request.getName())) {
            throw new RuntimeException("Quyền với tên này đã tồn tại");
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
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Quyền không tồn tại");
        }

        // Kiểm tra permission có được gán cho role nào không
        List<Role> rolesWithPermission = roleRepository.findByPermissions_Id(id);
        if (!rolesWithPermission.isEmpty()) {
            String roleNames = rolesWithPermission.stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Không thể xóa quyền: quyền đang được gán cho các vai trò: " + roleNames);
        }

        permissionRepository.deleteById(id);
    }
}
