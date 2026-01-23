package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public ReadUserDto toDTO(User user) {
        List<Long> projectIds = (user.getProjects() == null)
                ? List.of()
                : user.getProjects().stream().map(Project::getId).toList();

        List<Long> assignedTaskIds = (user.getAssignedTasks() == null)
                ? List.of()
                : user.getAssignedTasks().stream().map(Task::getId).toList();

        return new ReadUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAvatarUrl(),
                projectIds,
                assignedTaskIds
        );
    }
}
