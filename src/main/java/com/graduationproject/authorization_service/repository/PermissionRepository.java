package com.graduationproject.authorization_service.repository;

import com.graduationproject.authorization_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
   
    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    Optional<Permission> findByModuleAndAction(String module, String action);
}
