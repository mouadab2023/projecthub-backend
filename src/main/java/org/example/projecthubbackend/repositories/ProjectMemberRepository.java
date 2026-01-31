package org.example.projecthubbackend.repositories;


import org.example.projecthubbackend.entities.ProjectMember;
import org.example.projecthubbackend.enumerations.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByProject_Id(Long projectId);

    boolean existsByProjectIdAndUserIdAndRole(Long projectId, Long id, ProjectRole projectRole);

    void deleteByProject_Id(Long id);

    boolean existsByProjectIdAndUserId(Long projectId, Long id);
}
