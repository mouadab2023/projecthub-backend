package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.project.ProjectDetailsDto;
import org.example.projecthubbackend.dtos.project.ProjectDto;
import org.example.projecthubbackend.entities.Column;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.ProjectMember;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.example.projecthubbackend.mappers.ColumnMapper;
import org.example.projecthubbackend.mappers.ProjectMapper;
import org.example.projecthubbackend.mappers.ProjectMemberMapper;
import org.example.projecthubbackend.repositories.*;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    final ProjectRepository projectRepository;
    final ColumnRepository columnRepository;
    final ProjectMemberRepository projectMemberRepository;
    final AuthenticationService authenticationService;
    final ProjectMapper projectMapper;
    final ProjectMemberMapper projectMemberMapper;
    final ColumnMapper columnMapper;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;


    public List<ProjectDto> getAllProjects() {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        List<Project> projects = projectRepository.findAllByUserId(currentUser.getId());
        return projects.stream().map(projectMapper::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canViewProject(#id)")
    public ProjectDetailsDto findProjectDetailsById(@NotNull @Min(1) Long id) {
        Project project = projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<ProjectMember> members = projectMemberRepository.findAllByProject_Id(id);
        List<Column> columns = columnRepository.findAllByProject_Id(id);
        return ProjectDetailsDto.builder().id(project.getId()).name(project.getName()).columns(columns.stream().map(columnMapper::toDTO).collect(Collectors.toList())).members(members.stream().map(projectMemberMapper::toDTO).collect(Collectors.toList())).creationDate(project.getCreationDate()).build();
    }

    public ProjectDto createProject(@Valid ProjectDto projectDto) {
        Project newProject = Project.builder().name(projectDto.getName()).creationDate(LocalDate.now()).build();

        Project savedProject = projectRepository.save(newProject);

        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        ProjectMember projectMember = ProjectMember.builder().project(savedProject).user(currentUser).role(ProjectRole.OWNER).build();
        projectMemberRepository.save(projectMember);

        return projectMapper.toDTO(savedProject);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canEditProject(#id)")
    public ProjectDto updateProject(@NotNull @Min(1) Long id, @Valid ProjectDto projectDto) {
        Project project = projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!projectDto.getName().equals(project.getName())) project.setName(projectDto.getName());
        return projectMapper.toDTO(projectRepository.save(project));
    }

    @PreAuthorize(" hasRole('ROLE_ADMIN') or @projectSecurity.canDeleteProject(#id)")
    public void removeProject(@NotNull @Min(1) Long id) {
        projectMemberRepository.deleteByProject_Id(id);
        commentRepository.deleteAllByTask_Project_Id(id);
        itemRepository.deleteAllByTask_Project_Id(id);
        taskRepository.deleteByProject_Id(id);
        columnRepository.deleteByProject_Id(id);
        projectRepository.deleteById(id);
    }
}
