package org.example.projecthubbackend.repositories;


import org.example.projecthubbackend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
            select pm.project from ProjectMember pm where pm.user.id=:userId
            """)
    List<Project> findAllByUserId(Long userId);

}
