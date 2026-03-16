package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.ProjectMemberDto;
import org.example.projecthubbackend.entities.ProjectMember;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {
    public ProjectMemberDto toDTO(ProjectMember projectMember) {
        return ProjectMemberDto.builder().
                username(projectMember.getUser().getFirstName()+" "+projectMember.getUser().getLastName()).
                role(projectMember.getRole()).
                build();
    }
}
