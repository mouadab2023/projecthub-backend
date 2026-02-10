package org.example.projecthubbackend.repositories;

import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    void deleteByProject_Id(Long projectId);

    List<Task> findAllByProjectIdAndColumnId(Long project_id, Long column_id);

    List<Task> findAllByColumnId(Long columnId);

    List<Task> findAllByProjectId(Long projectId);
}
