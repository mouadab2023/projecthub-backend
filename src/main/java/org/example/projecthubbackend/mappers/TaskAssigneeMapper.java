package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.TaskAssigneeDto;
import org.example.projecthubbackend.entities.TaskAssignee;
import org.springframework.stereotype.Component;

@Component
public class TaskAssigneeMapper {
    public TaskAssigneeDto toDTO(TaskAssignee taskAssignee) {
        return TaskAssigneeDto.builder().
                username(taskAssignee.getUser().getUsername()+" "+taskAssignee.getUser().getLastName()).
                task(taskAssignee.getTask().getId()).
                assignedAt(taskAssignee.getAssignedAt()).
                build();
    }
}
