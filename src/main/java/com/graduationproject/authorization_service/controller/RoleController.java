package com.graduationproject.authorization_service.controller;

import com.graduationproject.authorization_service.dto.request.CreateRoleRequestDTO;
import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;
import com.graduationproject.authorization_service.service.RoleService;
import com.graduationproject.authorization_service.service.UserRoleService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoleResponseDTO>> getRolesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(roleService.getRolesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody CreateRoleRequestDTO request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody CreateRoleRequestDTO request) {
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> updateUserRoles(@PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        userRoleService.updateUserRoles(userId, roleIds);
        Map<String, String> response = Map.of("message", "Cập nhật vai trò thành công");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}/add")
    public ResponseEntity<Void> addRoleToUser(@PathVariable Long userId, @RequestParam Long roleId) {
        userRoleService.addRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}/remove")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId, @RequestParam Long roleId) {
        userRoleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
}
