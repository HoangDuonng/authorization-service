package com.graduationproject.authorization_service.repository;

import com.graduationproject.authorization_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    boolean existsByPermissions_Id(Long permissionId);

    long countByPermissions_Id(Long permissionId);

    List<Role> findByPermissions_Id(Long permissionId);
}
