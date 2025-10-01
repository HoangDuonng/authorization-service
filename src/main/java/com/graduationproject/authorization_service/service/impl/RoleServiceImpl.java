package com.graduationproject.authorization_service.service.impl;

import com.graduationproject.authorization_service.dto.request.CreateRoleRequestDTO;
import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;
import com.graduationproject.authorization_service.entity.Permission;
import com.graduationproject.authorization_service.entity.Role;
import com.graduationproject.authorization_service.entity.UserRole;
import com.graduationproject.authorization_service.mapper.RoleMapper;
import com.graduationproject.authorization_service.repository.PermissionRepository;
import com.graduationproject.authorization_service.repository.RoleRepository;
import com.graduationproject.authorization_service.repository.UserRoleRepository;
import com.graduationproject.authorization_service.service.RoleService;
import com.graduationproject.authorization_service.service.UserRoleService;
import com.graduationproject.authorization_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Long id) {
        try {
            return roleRepository.findById(id)
                    .map(roleMapper::toDto)
                    .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại với id: " + id));
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
                throw new RuntimeException("Vai trò với tên '" + request.getName() + "' đã tồn tại");
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
                    throw new RuntimeException("Một số quyền không tìm thấy. Vui lòng kiểm tra lại ID quyền.");
                }
                newRole.setPermissions(permissions);
            }

            Role savedRole = roleRepository.save(newRole);
            log.debug("Created role: {}", savedRole.getName());
            return roleMapper.toDto(savedRole);
        } catch (Exception e) {
            log.error("Error creating role: {}", request.getName(), e);
            throw new RuntimeException("Không thể tạo vai trò: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public RoleResponseDTO updateRole(Long id, CreateRoleRequestDTO request) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại với id: " + id));

            if (!role.getName().equals(request.getName()) &&
                    roleRepository.existsByName(request.getName())) {
                throw new RuntimeException("Vai trò với tên '" + request.getName() + "' đã tồn tại");
            }

            role.setName(request.getName());
            role.setDisplayName(request.getDisplayName());
            role.setDescription(request.getDescription());
            role.setIsSystem(request.getIsSystem());

            if (request.getPermissionIds() != null) {
                Set<Permission> permissions = new HashSet<>(
                        permissionRepository.findAllById(request.getPermissionIds()));
                if (permissions.size() != request.getPermissionIds().size()) {
                    throw new RuntimeException("Một số quyền không tìm thấy. Vui lòng kiểm tra lại ID quyền.");
                }
                role.setPermissions(permissions);
            }

            Role updatedRole = roleRepository.save(role);
            log.debug("Updated role: {}", updatedRole.getName());
            return roleMapper.toDto(updatedRole);
        } catch (Exception e) {
            log.error("Error updating role: {}", id, e);
            throw new RuntimeException("Không thể cập nhật vai trò: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại với id: " + id));

            if (role.getIsSystem()) {
                throw new RuntimeException("Không thể xóa vai trò hệ thống");
            }

            List<UserRole> userRoles = userRoleRepository.findById_RoleId(id);
            if (!userRoles.isEmpty()) {
                String userNames = userRoles.stream()
                        .map(userRole -> {
                            try {
                                return userService.getUserById(userRole.getId().getUserId()).getUsername();
                            } catch (Exception e) {
                                return "User-" + userRole.getId().getUserId();
                            }
                        })
                        .collect(Collectors.joining(", "));
                throw new RuntimeException(
                        "Không thể xóa vai trò: vai trò đang được gán cho các người dùng: " + userNames);
            }

            roleRepository.deleteById(id);
            log.debug("Deleted role with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting role: {}", id, e);
            throw new RuntimeException("Không thể xóa vai trò: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getRolesByUserId(Long userId) {
        List<UserRole> userRoles = userRoleService.getUserRoles(userId);
        return userRoles.stream()
                .map(UserRole::getRole)
                .map(roleMapper::toDto)
                .toList();
    }
}
