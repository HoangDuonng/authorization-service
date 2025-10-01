package com.graduationproject.authorization_service.service;

import com.graduationproject.authorization_service.entity.UserRole;
import java.util.List;

public interface UserRoleService {
    UserRole assignDefaultRole(Long userId, String role);

    List<UserRole> getUserRoles(Long userId);

    boolean hasRole(Long userId, String roleName);

    void updateUserRoles(Long userId, List<Long> roleIds);

    void addRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);
}
