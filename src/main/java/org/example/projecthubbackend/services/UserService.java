package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.user.InsertUserDto;
import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.dtos.user.UpdateUserDto;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.mappers.UserMapper;
import org.example.projecthubbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public ReadUserDto getUserById(Long id) {
        User user =userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }
    public ReadUserDto toReadUserDto(User user) {
        return userMapper.toDTO(user);
    }
    public ReadUserDto createUser(InsertUserDto userDto) {
        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .avatarUrl(userDto.getAvatarUrl())
                .build();
        User savedUser = userRepository.save(newUser);
        return userMapper.toDTO(savedUser);
    }
    public ReadUserDto updateUser(Long id, UpdateUserDto userDto) {
        User user =userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(!userDto.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(userDto.getFirstName());
        }
        if(!userDto.getLastName().equals(user.getLastName())) {
            user.setLastName(userDto.getLastName());
        }
        if(!userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (!userDto.getAvatarUrl().equals(user.getAvatarUrl())) {
            user.setAvatarUrl(userDto.getAvatarUrl());
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));    }
}
