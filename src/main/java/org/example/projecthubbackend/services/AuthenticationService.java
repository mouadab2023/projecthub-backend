package org.example.projecthubbackend.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.example.projecthubbackend.dtos.auth.LoginResponse;
import org.example.projecthubbackend.dtos.user.InsertUserDto;
import org.example.projecthubbackend.dtos.user.LoginUserDto;
import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.entities.RefreshToken;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.exceptions.UnauthorizedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;


    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserService userService, RefreshTokenService refreshTokenService, JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
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

        return (User) userService.loadUserByUsername(input.getEmail());
    }

    @Transactional
    public LoginResponse refresh(String refreshToken, HttpServletResponse response) {
        RefreshToken fetchedRefreshToken = refreshTokenService.findByToken(refreshToken).orElseThrow(UnauthorizedException::new);
        if (!fetchedRefreshToken.isValid() || refreshTokenService.isRefreshTokenExpired(fetchedRefreshToken)) {
            throw new UnauthorizedException();
        }
        fetchedRefreshToken.setValid(false);
        refreshTokenService.updateRefreshToken(fetchedRefreshToken);
        RefreshToken newRefreshtoken = refreshTokenService.generateRefreshToken(fetchedRefreshToken.getUser().getId());

        Cookie cookie = new Cookie("REFRESH_TOKEN", newRefreshtoken.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) Duration.between(Instant.now(), newRefreshtoken.getExpiryDate().toInstant()).getSeconds());

        response.addCookie(cookie);

        String newJwtToken = jwtService.generateToken(fetchedRefreshToken.getUser());

        return LoginResponse.builder()
                .token(newJwtToken)
                .expiresIn(jwtService.getExpirationTime()).build();
    }

    public LoginResponse login(LoginUserDto loginUserDto, HttpServletResponse response) {
        User authenticatedUser = this.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        RefreshToken newRefreshtoken = refreshTokenService.generateRefreshToken(authenticatedUser.getId());

        Cookie cookie = new Cookie("REFRESH_TOKEN", newRefreshtoken.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) Duration.between(Instant.now(), newRefreshtoken.getExpiryDate().toInstant()).getSeconds());

        response.addCookie(cookie);

        return LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .user(userService.toReadUserMinDto(authenticatedUser)).build();
    }
    @Transactional
    public void logout(String refreshToken,HttpServletResponse response) {
        RefreshToken fetchedRefreshToken = refreshTokenService.findByToken(refreshToken).orElseThrow(UnauthorizedException::new);
        fetchedRefreshToken.setValid(false);
        refreshTokenService.updateRefreshToken(fetchedRefreshToken);

        Cookie cookie = new Cookie("REFRESH_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

    }
}
