package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskAssigneeDto {
    private String username;

    @NotNull
    private Long task;



    private LocalDateTime assignedAt;
}
