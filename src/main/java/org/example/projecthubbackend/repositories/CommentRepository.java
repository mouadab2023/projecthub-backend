package org.example.projecthubbackend.repositories;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.projecthubbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByTask_Project_Id(Long taskProjectId);

    void deleteAllByTaskId(Long id);
}
