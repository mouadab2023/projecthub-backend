package org.example.projecthubbackend.dtos.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.ColumnDto;
import org.example.projecthubbackend.dtos.ProjectMemberDto;
import org.hibernate.sql.Update;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailsDto {
    @NotNull(groups = Update.class)
    Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate creationDate;

    List<ProjectMemberDto> members;

    List<ColumnDto> columns;

}
