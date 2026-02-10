package org.example.projecthubbackend.dtos.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.projecthubbackend.dtos.ItemDto;
import org.example.projecthubbackend.dtos.TaskAssigneeDto;
import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.enumerations.Priority;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TaskDetailsDto {
    @NotNull(groups = Update.class)
    Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private Priority priority;

    @NotNull
    private Long project;

    @NotNull
    private Long column;

    private List<ItemDto> items;

    private List<TaskAssigneeDto> assignees;
}

