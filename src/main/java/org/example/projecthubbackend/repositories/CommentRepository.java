package org.example.projecthubbackend.repositories;

import org.example.projecthubbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByTaskId(Long id);

    void deleteByTaskProjectId(Long projectId);

    List<Comment> findAllByTaskId(Long taskId);

    List<Comment> findAllByTaskColumnProjectIdAndTaskColumnIdAndTaskId(Long taskColumnProjectId, Long taskColumnId, Long taskId);

    Optional<Comment> findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(Long projectId, Long columnId, Long taskId, Long id);
}
