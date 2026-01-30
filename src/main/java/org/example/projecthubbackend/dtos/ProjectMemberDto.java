package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.enumerations.ProjectRole;
@Data
@Builder
public class ProjectMemberDto {
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private Long project;

    @NotNull
    private Long user;

    @NotNull
    private ProjectRole role;
}
