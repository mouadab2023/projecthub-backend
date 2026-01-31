package org.example.projecthubbackend.controllers;


import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.repositories.UserRepository;
import org.example.projecthubbackend.services.UserService;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    public ResponseEntity<ReadUserDto> authenticatedUser() {
        User currentUser =authenticationService.getCurrentUserFromSecurityContext();
        ReadUserDto authenticatedUser = userService.toReadUserDto(currentUser);
        return ResponseEntity.ok(authenticatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<ReadUserDto>> getAllUsers() {
        List<ReadUserDto> allUsers= userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

}
