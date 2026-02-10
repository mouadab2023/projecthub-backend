package org.example.projecthubbackend.dtos.auth;

import lombok.Builder;
import lombok.Data;
import org.example.projecthubbackend.dtos.user.ReadUserDto;

@Data
@Builder
public class LoginResponse {
    ReadUserDto user;
    private String token;
    private long expiresIn;
}
