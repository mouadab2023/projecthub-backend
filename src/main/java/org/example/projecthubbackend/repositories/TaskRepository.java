package org.example.projecthubbackend.repositories;


import  jakarta.persistence.LockModeType;
import org.example.projecthubbackend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProjectIdAndColumnId(Long project_id, Long column_id);

    List<Task> findAllByColumnIdOrderByPositionAsc(Long columnId);

    List<Task> findAllByProjectId(Long projectId);


    Long findProjectIdById(Long id);

    void deleteByColumnId(Long id);

    void deleteByProjectId(Long id);

    Optional<Task> findByProjectIdAndColumnIdAndId(Long projectId, Long columnId, Long id);


    @Modifying
    @Query("""
            UPDATE Task t
            SET t.position = t.position - 1
            WHERE t.project.id = :projectId and t.column.id=:columnId
            AND t.position > :oldPosition
            """)
    void shiftLeft(Long projectId, Long columnId, int oldPosition);

    @Modifying
    @Query("""
            UPDATE Task t
            SET t.position = t.position + 1
            WHERE t.project.id = :projectId and t.column.id=:columnId
            AND t.position >= :newPosition
            """)
    void shiftRight(Long projectId, Long columnId, int newPosition);

    @Query("""
            SELECT max(t.position)
            from Task t
            where t.column.id=:columnId
            and t.column.project.id=:projectId
            """)
    Integer findMaxPosition(Long projectId, Long columnId);

    @Modifying
    @Query("""
            UPDATE Task t
            SET t.position = t.position - 1
            WHERE  t.column.id=:columnId and t.column.project.id=:projectId
            AND t.position > :oldPosition
            AND t.position <= :newPosition
            """)
    void shiftLeft(Long projectId, Long columnId, int oldPosition, int newPosition);


    @Modifying
    @Query("""
            UPDATE Task t
            SET t.position = t.position + 1
            WHERE  t.column.id=:columnId and t.column.project.id=:projectId
            AND t.position >= :newPosition
            AND t.position < :oldPosition
            """)
    void shiftRight(Long projectId, Long columnId, int oldPosition, int newPosition);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT t FROM Task t
            WHERE t.project.id = :projectId
              AND t.column.id = :columnId
              AND t.id = :taskId
            """)
    Optional<Task> lockTask(Long projectId, Long columnId, Long taskId);
}