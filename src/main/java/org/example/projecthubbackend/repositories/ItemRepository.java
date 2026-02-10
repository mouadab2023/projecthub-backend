package org.example.projecthubbackend.repositories;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.projecthubbackend.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    void deleteAllByTask_Project_Id(Long taskProjectId);

    List<Item> findAllByTaskId(Long id);

    void deleteAllByTaskId(Long taskId);
}
