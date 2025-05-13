package com.graduationproject.authorization_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.Set;

@Data
public class CreateRoleRequestDTO {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Role name must be in lowercase letters, numbers and underscores only")
    private String name;

    @NotBlank(message = "Display name is required")
    private String displayName;

    private String description;

    private Boolean isSystem = false;

    private Set<Integer> permissionIds;
}
