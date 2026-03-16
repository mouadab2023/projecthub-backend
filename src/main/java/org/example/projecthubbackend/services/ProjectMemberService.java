package org.example.projecthubbackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.example.projecthubbackend.repositories.ProjectMemberRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    @Cacheable(value = "projectRoles", key = "#projectId + '-' + #userId")
    public Set<ProjectRole> getUserRoles(Long projectId, Long userId) {
        return projectMemberRepository.findRolesByProjectIdAndUserId(projectId, userId);
    }
}
