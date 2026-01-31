package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.dtos.user.ReadUserMinDto;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public ReadUserDto toDTO(User user) {


        return new ReadUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getRoles()
        );
    }

    public ReadUserMinDto toReadUserMinDto(User user) {
        return new ReadUserMinDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getRoles()
                );
    }
}
