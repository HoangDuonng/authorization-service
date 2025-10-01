package com.graduationproject.authorization_service.service.impl;

import com.graduationproject.authorization_service.entity.Role;
import com.graduationproject.authorization_service.entity.UserRole;
import com.graduationproject.authorization_service.entity.UserRoleId;
import com.graduationproject.authorization_service.repository.RoleRepository;
import com.graduationproject.authorization_service.repository.UserRoleRepository;
import com.graduationproject.authorization_service.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserRole assignDefaultRole(Long userId, String role) {
        log.debug("Assigning default role (CLIENT) to user: {}", userId);

        Role clientRole = roleRepository.findByName("client")
                .orElseThrow(() -> new RuntimeException("Default role CLIENT not found"));

        if (userRoleRepository.existsById_UserIdAndId_RoleId(userId, clientRole.getId())) {
            log.debug("User {} already has CLIENT role", userId);
            return userRoleRepository.findById_UserId(userId).get(0);
        }

        UserRole userRole = new UserRole();
        userRole.setId(new UserRoleId(userId, clientRole.getId()));
        userRole.setRole(clientRole);
        userRole.setGrantedBy(userId); // Self-granted for default role

        UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("Assigned CLIENT role to user: {}", userId);

        return savedUserRole;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> getUserRoles(Long userId) {
        return userRoleRepository.findById_UserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(Long userId, String roleName) {
        return userRoleRepository.findById_UserId(userId).stream()
                .anyMatch(userRole -> userRole.getRole().getName().equals(roleName));
    }

    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        List<UserRole> currentRoles = userRoleRepository.findById_UserId(userId);
        userRoleRepository.deleteAll(currentRoles);
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
            UserRole userRole = new UserRole();
            userRole.setId(new UserRoleId(userId, roleId));
            userRole.setRole(role);
            userRole.setGrantedBy(userId);
            userRoleRepository.save(userRole);
        }
    }

    @Override
    @Transactional
    public void addRoleToUser(Long userId, Long roleId) {
        if (userRoleRepository.existsById_UserIdAndId_RoleId(userId, roleId)) {
            return;
        }
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        UserRole userRole = new UserRole();
        userRole.setId(new UserRoleId(userId, roleId));
        userRole.setRole(role);
        userRole.setGrantedBy(userId);
        userRoleRepository.save(userRole);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        UserRoleId userRoleId = new UserRoleId(userId, roleId);
        userRoleRepository.deleteById(userRoleId);
    }
}
