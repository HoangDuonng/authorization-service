package com.graduationproject.authorization_service.repository;

import com.graduationproject.authorization_service.entity.UserRole;
import com.graduationproject.authorization_service.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findById_UserId(Long userId);

    boolean existsById_UserIdAndId_RoleId(Long userId, Long roleId);

    boolean existsById_RoleId(Long roleId);

    List<UserRole> findById_RoleId(Long roleId);
}
