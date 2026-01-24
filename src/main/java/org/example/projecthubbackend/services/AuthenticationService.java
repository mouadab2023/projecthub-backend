package org.example.projecthubbackend.services;

import org.example.projecthubbackend.dtos.user.InsertUserDto;
import org.example.projecthubbackend.dtos.user.LoginUserDto;
import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public ReadUserDto signup(InsertUserDto input) {
       return userService.createUser(input);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return (User)userService.loadUserByUsername(input.getEmail());
    }
}
