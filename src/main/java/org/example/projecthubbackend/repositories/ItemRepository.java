package org.example.projecthubbackend.repositories;

import org.example.projecthubbackend.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    void deleteAllByTask_Project_Id(Long taskProjectId);
}
