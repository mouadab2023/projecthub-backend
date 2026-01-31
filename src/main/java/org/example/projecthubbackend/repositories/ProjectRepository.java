package org.example.projecthubbackend.repositories;


import org.example.projecthubbackend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {}
