package com.graduationproject.authorization_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.Set;

@Data
public class CreateRoleRequestDTO {
    @NotBlank(message = "Tên vai trò là bắt buộc")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Tên vai trò chỉ được chứa chữ thường, số và dấu gạch dưới")
    private String name;

    @NotBlank(message = "Tên hiển thị là bắt buộc")
    private String displayName;

    private String description;

    private Boolean isSystem = false;

    private Set<Long> permissionIds;
}
