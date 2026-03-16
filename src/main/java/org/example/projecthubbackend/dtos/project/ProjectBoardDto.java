package org.example.projecthubbackend.dtos.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.ProjectMemberDto;
import org.example.projecthubbackend.dtos.column.ColumnDetailsDto;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBoardDto {
    Long id;
    List<ProjectMemberDto> members;
    List<ColumnDetailsDto> columns;
    private String name;
    private LocalDate creationDate;
}
