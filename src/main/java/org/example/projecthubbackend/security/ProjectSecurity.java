package org.example.projecthubbackend.security;

import jakarta.transaction.Transactional;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.example.projecthubbackend.repositories.ProjectMemberRepository;
import org.example.projecthubbackend.repositories.TaskAssigneeRepository;
import org.example.projecthubbackend.repositories.TaskRepository;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.stereotype.Component;
@Transactional
@Component("projectSecurity")
public class ProjectSecurity {

    private final ProjectMemberRepository projectMemberRepository;
    private final AuthenticationService authenticationService;
    private final TaskRepository taskRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;

    public ProjectSecurity(ProjectMemberRepository projectMemberRepository,
                           AuthenticationService authenticationService, TaskRepository taskRepository, TaskAssigneeRepository taskAssigneeRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.authenticationService = authenticationService;
        this.taskRepository = taskRepository;
        this.taskAssigneeRepository = taskAssigneeRepository;
    }

    public boolean isOwner(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,
                currentUser.getId(),
                ProjectRole.OWNER
        );
    }
    public boolean isManager(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,
                currentUser.getId(),
                ProjectRole.COLLABORATOR
        );
    }

    public boolean isCollaborator(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,
                currentUser.getId(),
                ProjectRole.COLLABORATOR
        );
    }
    public boolean isViewer(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,
                currentUser.getId(),
                ProjectRole.VIEWER
        );
    }
    public boolean isProjectMember(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserId(
                projectId,
                currentUser.getId()
        );
    }
    public boolean isTaskAssignee(Long taskId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return taskAssigneeRepository.existsByTaskIdAndUserId(taskId,currentUser.getId());
    }


    public boolean canEditProject(Long projectId) {
        return  this.isOwner(projectId);
    }
    public boolean canViewProject(Long projectId) {
        return  this.isProjectMember(projectId);
    }
    public boolean canDeleteProject(Long projectId) {
        return  this.isOwner(projectId);
    }


    public boolean canCreateTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return this.isProjectMember(task.getProject().getId());
    }
    public boolean canEditTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return this.isTaskAssignee(taskId) || this.isOwner(task.getProject().getId()) || this.isManager(task.getProject().getId());
    }
    public boolean canViewTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return this.isProjectMember(task.getProject().getId());
    }
    public boolean canDeleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return  this.isManager(task.getProject().getId()) || this.isOwner(task.getProject().getId());
    }


}

