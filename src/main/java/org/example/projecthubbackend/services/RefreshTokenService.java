package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.example.projecthubbackend.entities.RefreshToken;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.repositories.RefreshTokenRepository;
import org.example.projecthubbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Getter
    @Value("${security.jwt.refresh.expiration-time}")
    private Long refreshTokenDurationMs;

    @Autowired
    RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken generateRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(new Date(System.currentTimeMillis() + refreshTokenDurationMs))
                .valid(true)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().before(new Date(System.currentTimeMillis()));
    }

    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

}
