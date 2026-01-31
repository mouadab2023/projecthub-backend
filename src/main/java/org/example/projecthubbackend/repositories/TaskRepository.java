package org.example.projecthubbackend.repositories;

import org.example.projecthubbackend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    void deleteByProject_Id(Long projectId);
}
