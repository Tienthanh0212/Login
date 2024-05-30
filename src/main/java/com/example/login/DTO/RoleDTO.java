package com.example.login.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    @NotBlank(message = "{role.notblank}")
    private String name;
}
