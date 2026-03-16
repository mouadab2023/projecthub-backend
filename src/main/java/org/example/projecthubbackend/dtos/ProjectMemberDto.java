package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.projecthubbackend.enumerations.ProjectRole;

@Data
@Builder
public class ProjectMemberDto {
    private String username;
    @NotNull
    private ProjectRole role;
}
