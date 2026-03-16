package org.example.projecthubbackend.security;

import org.example.projecthubbackend.enumerations.ProjectRole;
import org.example.projecthubbackend.repositories.*;
import org.example.projecthubbackend.services.ProjectMemberService;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component("projectSecurity")
public class ProjectSecurity {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberService projectMemberService;
    private final AuthenticationService authenticationService;
    private final TaskRepository taskRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final ColumnRepository columnRepository;

    public ProjectSecurity(ProjectMemberRepository projectMemberRepository,
                           AuthenticationService authenticationService, TaskRepository taskRepository, TaskAssigneeRepository taskAssigneeRepository, ColumnRepository columnRepository, ProjectRepository projectRepository, ProjectMemberService projectMemberService) {
        this.projectMemberRepository = projectMemberRepository;
        this.authenticationService = authenticationService;
        this.taskRepository = taskRepository;
        this.taskAssigneeRepository = taskAssigneeRepository;
        this.columnRepository = columnRepository;
        this.projectMemberService = projectMemberService;
    }

    private Long getCurrentUserId() {
        return authenticationService.getCurrentUserFromSecurityContext().getId();
    }

    private boolean hasRole(Long projectId, ProjectRole... roles) {
        Set<ProjectRole> userRoles = projectMemberService.getUserRoles(projectId, getCurrentUserId());
        return userRoles.stream().anyMatch(List.of(roles)::contains);
    }

    public boolean isTaskAssignee(Long taskId) {
        return taskAssigneeRepository.existsByTaskIdAndUserId(taskId, getCurrentUserId());
    }

    public boolean canEditProject(Long projectId) {
        return hasRole(projectId, ProjectRole.OWNER);
    }

    public boolean canViewProject(Long projectId) {
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER, ProjectRole.COLLABORATOR, ProjectRole.VIEWER);
    }

    public boolean canDeleteProject(Long projectId) {
        return hasRole(projectId, ProjectRole.OWNER);
    }


    public boolean canCreateTask(Long taskId) {
        Long projectId = taskRepository.findProjectIdById(taskId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER, ProjectRole.COLLABORATOR);
    }

    public boolean canEditTask(Long taskId) {
        Long projectId = taskRepository.findProjectIdById(taskId);
        return this.isTaskAssignee(taskId) || hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER);
    }

    public boolean canViewTask(Long taskId) {
        Long projectId = taskRepository.findProjectIdById(taskId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER, ProjectRole.COLLABORATOR, ProjectRole.VIEWER);
    }

    public boolean canDeleteTask(Long taskId) {
        Long projectId = taskRepository.findProjectIdById(taskId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER);
    }


    public boolean canCreateColumn(Long ColumnId) {
        Long projectId = columnRepository.findProjectIdById(ColumnId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER, ProjectRole.COLLABORATOR);
    }

    public boolean canEditColumn(Long ColumnId) {
        Long projectId = columnRepository.findProjectIdById(ColumnId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER);
    }

    public boolean canViewColumn(Long ColumnId) {
        Long projectId = columnRepository.findProjectIdById(ColumnId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER, ProjectRole.COLLABORATOR, ProjectRole.VIEWER);
    }

    public boolean canDeleteColumn(Long ColumnId) {
        Long projectId = columnRepository.findProjectIdById(ColumnId);
        return hasRole(projectId, ProjectRole.OWNER, ProjectRole.MANAGER);
    }

}

