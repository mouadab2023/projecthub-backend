package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.project.ProjectDto;
import org.example.projecthubbackend.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDto toDTO(Project project) {
        return ProjectDto.builder().
                id(project.getId()).
                name(project.getName()).
                creationDate(project.getCreationDate()).
                build();
    }

    public Project toEntityBasics(ProjectDto projectDto) {
        return Project.builder().
                id(projectDto.getId()).
                name(projectDto.getName()).
                creationDate(projectDto.getCreationDate()).
                build();
    }
}
