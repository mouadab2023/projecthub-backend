package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.task.TaskDto;
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
                .position(task.getPosition())
                .build();
    }

}
