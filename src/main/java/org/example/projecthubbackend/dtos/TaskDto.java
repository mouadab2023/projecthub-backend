package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.projecthubbackend.enumerations.Priority;
import org.example.projecthubbackend.enumerations.Status;
import org.hibernate.sql.Update;

import java.time.LocalDate;

@Data
@Builder
public class TaskDto {
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

    @Builder.Default
    private Status status = Status.TODO;

    @NotNull
    private Long project;

    private Long assignee;
}
