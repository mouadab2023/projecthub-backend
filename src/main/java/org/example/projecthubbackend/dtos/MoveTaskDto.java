package org.example.projecthubbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveTaskDto {
    private Long targetColumnId;
    private Integer newPosition;
}
