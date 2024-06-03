package com.example.login.DTO.User;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "{username.notblank}")
    private String username;

    @NotBlank(message = "{password.notblank}")
    private String password;
}
