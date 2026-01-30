package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotNull;
import org.example.projecthubbackend.dtos.groups.Update;

import java.time.LocalDateTime;

public class TaskAssigneeDto {
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private Long project;

    @NotNull
    private Long task;

    private LocalDateTime assignedAt;
}
