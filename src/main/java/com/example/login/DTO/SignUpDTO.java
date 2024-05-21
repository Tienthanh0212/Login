package com.example.login.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpDTO {

    @NotBlank(message = "{username.notblank}")
    private String username;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 8, message = "{password.size}")
    private String password;

     @NotBlank(message = "{confirmPassword.notblank}")
    private String confirmPassword;
}
