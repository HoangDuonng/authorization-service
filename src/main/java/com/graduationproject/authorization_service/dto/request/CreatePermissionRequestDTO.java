package com.graduationproject.authorization_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreatePermissionRequestDTO {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Name must contain only lowercase letters, numbers and underscores")
    private String name;

    @NotBlank(message = "Display name is required")
    private String displayName;

    private String description;

    @NotBlank(message = "Module is required")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Module must contain only lowercase letters, numbers and underscores")
    private String module;

    @NotBlank(message = "Action is required")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Action must contain only lowercase letters, numbers and underscores")
    private String action;
}
