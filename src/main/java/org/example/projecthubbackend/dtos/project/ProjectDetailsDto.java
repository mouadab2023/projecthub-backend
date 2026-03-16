package org.example.projecthubbackend.dtos.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.ProjectMemberDto;
import org.example.projecthubbackend.dtos.column.ColumnDto;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailsDto {
    Long id;
    List<ProjectMemberDto> members;
    List<ColumnDto> columns;
    private String name;
    private LocalDate creationDate;

}
