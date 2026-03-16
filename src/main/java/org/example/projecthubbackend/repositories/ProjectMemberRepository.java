package org.example.projecthubbackend.repositories;


import org.example.projecthubbackend.entities.ProjectMember;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByProjectId(Long projectId);

    boolean existsByProjectIdAndUserIdAndRole(Long projectId, Long id, ProjectRole projectRole);

    void deleteByProject_Id(Long id);

    boolean existsByProjectIdAndUserId(Long projectId, Long id);

    List<ProjectMember> findAllByUserId(Long userId);

    boolean existsByProjectIdAndUserIdAndRoleIn(Long projectId, Long userId, Collection<ProjectRole> roles);

    Set<ProjectRole> findRolesByProjectIdAndUserId(Long projectId, Long userId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);
}
