package org.example.projecthubbackend.dtos.column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.task.TaskDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDetailsDto {
    Long id;

    private String name;

    private int position;

    private List<TaskDto> tasks;
}
