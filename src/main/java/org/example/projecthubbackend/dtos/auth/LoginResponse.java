package org.example.projecthubbackend.dtos.auth;

import lombok.Builder;
import lombok.Data;
import org.example.projecthubbackend.dtos.user.ReadUserMinDto;

@Data
@Builder
public class LoginResponse {
    ReadUserMinDto user;
    private String token;
    private long expiresIn;
}
