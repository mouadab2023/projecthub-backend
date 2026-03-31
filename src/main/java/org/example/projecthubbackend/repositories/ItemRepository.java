package org.example.projecthubbackend.repositories;

import org.example.projecthubbackend.entities.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByTaskId(Long id);

    void deleteAllByTaskId(Long taskId);


    void deleteByTaskColumnId(Long id);

    void deleteByTaskProjectId(Long projectId);

    @Query("SELECT max(i.position) FROM Item i where i.task.id=:taskId")
    Integer findMaxPosition(Long taskId);

    List<Item> findAllByTaskColumnProjectIdAndTaskColumnIdAndTaskIdOrderByPositionAsc(Long taskColumnProjectId, Long taskColumnId, Long taskId);

    Optional<Item> findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(Long projectId, Long columnId, Long taskId, Long id);

    @Modifying
    @Query("""
            UPDATE Item i
            SET i.position = i.position - 1
            WHERE i.task.id=:taskId and i.task.column.id=:columnId and i.task.column.project.id=:projectId
            AND i.position > :oldPosition
            AND i.position <= :newPosition
            """)
    void shiftLeft(Long projectId, Long columnId, Long taskId, int oldPosition, int newPosition);


    @Modifying
    @Query("""
            UPDATE Item i
            SET i.position = i.position + 1
            WHERE i.task.id=:taskId and i.task.column.id=:columnId and i.task.column.project.id=:projectId
            AND i.position >= :newPosition
            AND i.position < :oldPosition
            """)
    void shiftRight(Long projectId, Long columnId, Long taskId, int oldPosition, int newPosition);
}
