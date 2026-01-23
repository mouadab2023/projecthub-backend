package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.TaskDto;
import org.example.projecthubbackend.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDto toDTO(Task task) {
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .project(task.getProject() != null ? task.getProject().getId() : null)
                .assignee(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .build();
    }

    public Task toEntityBasics(TaskDto taskDTO) {
        return Task.builder().
                title(taskDTO.getTitle()).
                description(taskDTO.getDescription()).
                dueDate(taskDTO.getDueDate()).
                priority(taskDTO.getPriority()).
                status(taskDTO.getStatus()).
                build();
    }
}
