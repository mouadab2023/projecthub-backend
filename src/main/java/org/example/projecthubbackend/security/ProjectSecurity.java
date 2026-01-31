package org.example.projecthubbackend.security;

import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.example.projecthubbackend.repositories.ProjectMemberRepository;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.stereotype.Component;

@Component("projectSecurity")
public class ProjectSecurity {

    private final ProjectMemberRepository projectMemberRepository;
    private final AuthenticationService authenticationService;

    public ProjectSecurity(ProjectMemberRepository projectMemberRepository,
                           AuthenticationService authenticationService) {
        this.projectMemberRepository = projectMemberRepository;
        this.authenticationService = authenticationService;
    }

    public boolean isOwner(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,
                currentUser.getId(),
                ProjectRole.OWNER
        );
    }
    public boolean isMember(Long projectId) {
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        return projectMemberRepository.existsByProjectIdAndUserId(
                projectId,
                currentUser.getId()
        );
    }
}

