package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.ProjectMemberDto;
import org.example.projecthubbackend.entities.ProjectMember;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {
    public ProjectMemberDto toDTO(ProjectMember projectMember) {
        return ProjectMemberDto.builder().
                id(projectMember.getId()).
                project(projectMember.getUser().getId()).
                user(projectMember.getUser().getId()).
                role(projectMember.getRole()).
                build();
    }
}
