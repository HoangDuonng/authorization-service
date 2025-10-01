package com.graduationproject.authorization_service.controller;

import com.graduationproject.authorization_service.dto.response.RoleResponseDTO;
import com.graduationproject.authorization_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<List<RoleResponseDTO>> getUserRoles(@PathVariable Long userId) {
        return ResponseEntity.ok(roleService.getRolesByUserId(userId));
    }
}
