package com.graduationproject.authorization_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreatePermissionRequestDTO {
    @NotBlank(message = "Tên quyền là bắt buộc")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Tên quyền chỉ được chứa chữ thường, số và dấu gạch dưới")
    private String name;

    @NotBlank(message = "Tên hiển thị là bắt buộc")
    private String displayName;

    private String description;

    @NotBlank(message = "Module là bắt buộc")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Module chỉ được chứa chữ thường, số và dấu gạch dưới")
    private String module;

    @NotBlank(message = "Hành động là bắt buộc")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Hành động chỉ được chứa chữ thường, số và dấu gạch dưới")
    private String action;
}
