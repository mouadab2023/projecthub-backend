package org.example.projecthubbackend.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.projecthubbackend.dtos.auth.LoginResponse;
import org.example.projecthubbackend.dtos.user.InsertUserDto;
import org.example.projecthubbackend.dtos.user.LoginUserDto;
import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.exceptions.MissingRefreshTokenException;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ReadUserDto> signup(@RequestBody InsertUserDto insertUserDto) {
        ReadUserDto registeredUser = authenticationService.signup(insertUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        LoginResponse loginResponse = authenticationService.login(loginUserDto, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) throw new MissingRefreshTokenException();
        LoginResponse loginResponse = authenticationService.refresh(refreshToken, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken,HttpServletResponse response) {
        if (refreshToken == null) throw new MissingRefreshTokenException();
        authenticationService.logout(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

}
