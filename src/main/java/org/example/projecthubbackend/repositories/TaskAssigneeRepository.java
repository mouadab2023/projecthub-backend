package org.example.projecthubbackend.repositories;

import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee,Long> {
    List<TaskAssignee> findByTask(Task task);

    boolean existsByTaskIdAndUserId(Long taskId, Long id);
}
