package org.example.projecthubbackend.dtos.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.groups.Create;
import org.example.projecthubbackend.enumerations.Priority;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    Long id;

    @NotBlank(groups = Create.class)
    private String title;

    @NotBlank(groups = Create.class)
    private String description;

    private Integer position;

    @NotNull(groups = Create.class)
    private LocalDate dueDate;

    @NotNull(groups = Create.class)
    private Priority priority;

}
