package org.example.projecthubbackend.repositories;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.projecthubbackend.entities.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    List<Column> findAllByProject_Id(Long id);

    void deleteByProject_Id(Long projectId);
}
