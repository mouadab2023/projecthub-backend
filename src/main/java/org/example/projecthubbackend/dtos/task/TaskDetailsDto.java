package org.example.projecthubbackend.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.CommentDto;
import org.example.projecthubbackend.dtos.ItemDto;
import org.example.projecthubbackend.dtos.TaskAssigneeDto;
import org.example.projecthubbackend.enumerations.Priority;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsDto {
    Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private Priority priority;

    private Long project;

    private Long column;

    private List<ItemDto> items;

    private List<CommentDto> comments;

    private List<TaskAssigneeDto> assignees;
}

