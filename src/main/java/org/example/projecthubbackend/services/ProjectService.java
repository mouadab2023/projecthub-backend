package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.column.ColumnDetailsDto;
import org.example.projecthubbackend.dtos.project.ProjectBoardDto;
import org.example.projecthubbackend.dtos.project.ProjectDetailsDto;
import org.example.projecthubbackend.dtos.project.ProjectDto;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.entities.Column;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.ProjectMember;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.example.projecthubbackend.mappers.ColumnMapper;
import org.example.projecthubbackend.mappers.ProjectMapper;
import org.example.projecthubbackend.mappers.ProjectMemberMapper;
import org.example.projecthubbackend.mappers.TaskMapper;
import org.example.projecthubbackend.repositories.ColumnRepository;
import org.example.projecthubbackend.repositories.ProjectMemberRepository;
import org.example.projecthubbackend.repositories.ProjectRepository;
import org.example.projecthubbackend.repositories.TaskRepository;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final TaskMapper taskMapper;


    public List<ProjectDto> getAllProjects() {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        List<Project> projects = projectRepository.findAllByUserId(currentUser.getId());
        return projects.stream().map(projectMapper::toDTO).collect(Collectors.toList());
    }

    public ProjectBoardDto getBoard(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found"));
        List<ProjectMember> members = projectMemberRepository.findAllByProjectId(id);
        List<Column> columns = columnRepository.findAllByProjectIdOrderByPositionAsc(id);
        List<ColumnDetailsDto> columnDetailsDtos=new ArrayList<>();
        for( Column column : columns) {
            List<TaskDto> tasks = taskRepository.findAllByColumnIdOrderByPositionAsc(column.getId()).stream().map(taskMapper::toDTO).toList();
            columnDetailsDtos.add(ColumnDetailsDto.builder().
                    id(column.getId()).
                    name(column.getName()).
                    position(column.getPosition()).
                    tasks(tasks).
                    build());
        }
        return ProjectBoardDto.builder()
                .id(id)
                .name(project.getName())
                .members(members.stream().map(projectMemberMapper::toDTO).collect(Collectors.toList()))
                .columns(columnDetailsDtos).
                creationDate(project.getCreationDate()).
                build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canViewProject(#id)")
    public ProjectDetailsDto findProjectDetailsById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found"));
        List<ProjectMember> members = projectMemberRepository.findAllByProjectId(id);
        List<Column> columns = columnRepository.findAllByProjectIdOrderByPositionAsc(id);
        return ProjectDetailsDto.builder().id(project.getId()).name(project.getName()).columns(columns.stream().map(columnMapper::toDTO).collect(Collectors.toList())).members(members.stream().map(projectMemberMapper::toDTO).collect(Collectors.toList())).creationDate(project.getCreationDate()).build();
    }

    public ProjectDto createProject(ProjectDto projectDto) {
        Project newProject = Project.builder().name(projectDto.getName()).creationDate(LocalDate.now()).build();

        Project savedProject = projectRepository.save(newProject);

        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        ProjectMember projectMember = ProjectMember.builder().project(savedProject).user(currentUser).role(ProjectRole.OWNER).build();
        projectMemberRepository.save(projectMember);

        return projectMapper.toDTO(savedProject);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canEditProject(#id)")
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (projectDto.getName() != null)
            if (!projectDto.getName().equals(project.getName()))
                project.setName(projectDto.getName());
        return projectMapper.toDTO(projectRepository.save(project));
    }

    @PreAuthorize(" hasRole('ROLE_ADMIN') or @projectSecurity.canDeleteProject(#id)")
    public void removeProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found"));
        projectRepository.delete(project);
    }
}
