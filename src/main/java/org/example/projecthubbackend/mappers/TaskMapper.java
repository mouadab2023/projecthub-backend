package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.TaskDto;
import org.example.projecthubbackend.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDto toDTO(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .project(task.getProject() != null ? task.getProject().getId() : null)
                .build();
    }

    public Task toEntityBasics(TaskDto taskDTO) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle()).
                description(taskDTO.getDescription()).
                dueDate(taskDTO.getDueDate()).
                priority(taskDTO.getPriority()).
                build();
    }
}
