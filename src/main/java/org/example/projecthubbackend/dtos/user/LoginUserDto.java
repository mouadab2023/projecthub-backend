package org.example.projecthubbackend.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginUserDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
