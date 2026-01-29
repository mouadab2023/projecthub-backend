package org.example.projecthubbackend.mappers;

import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.ProjectDto;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public  ProjectDto toDTO(Project project){
        return ProjectDto.builder().
                id(project.getId()).
                name(project.getName()).
                startDate(project.getStartDate()).
                endDate(project.getEndDate()).
                owner(project.getOwner() != null ? project.getOwner().getId() : null).
                tasks(project.getTasks() != null ? project.getTasks().stream().map(Task::getId).collect(Collectors.toList()) : List.of())
                .build();
    }

    public Project toEntityBasics(ProjectDto projectDto){
        return Project.builder().
                id(projectDto.getId()).
                name(projectDto.getName()).
                startDate(projectDto.getStartDate()).
                endDate(projectDto.getEndDate()).
                build();
    }
}
