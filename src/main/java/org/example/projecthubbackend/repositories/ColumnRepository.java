package org.example.projecthubbackend.repositories;


import jakarta.persistence.LockModeType;
import org.example.projecthubbackend.entities.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    List<Column> findAllByProjectIdOrderByPositionAsc(Long projectId);

    List<Column> findAllByProjectIdOrderByPositionDesc(Long projectId);

    @Query("SELECT max(c.position) FROM Column c where c.project.id=:projectId")
    Integer findMaxPosition(Long projectId);

    Long findProjectIdById(Long columnId);

    Optional<Column> findByProjectIdAndId(Long projectId, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT c FROM Column c
                WHERE c.project.id = :projectId
                AND c.id = :columnId
            """)
    Optional<Column> lockColumn(Long projectId, Long columnId);

    void deleteByProjectId(Long id);

    @Modifying
    @Query("""
                UPDATE Column c
                SET c.position = c.position - 1
                WHERE c.project.id = :projectId
                AND c.position > :oldPosition
                AND c.position <= :newPosition
            """)
    void shiftLeft(Long projectId, int oldPosition, int newPosition);


    @Modifying
    @Query("""
                 UPDATE Column c
                 SET c.position = c.position + 1
                 WHERE c.project.id = :projectId
                 AND c.position >= :newPosition
                 AND c.position < :oldPosition
            """)
    void shiftRight(Long projectId, int oldPosition, int newPosition);

    @Modifying
    @Query("""
                 UPDATE Column c
                 SET c.position = c.position - 1
                 WHERE c.project.id = :projectId
                 AND c.position > :oldPosition
            """)
    void shiftLeft(Long projectId, int oldPosition);

}