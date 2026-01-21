package org.example.projecthubbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Valid
    @NotNull
    @ManyToOne
    private User owner;

    @Builder.Default
    @Valid
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task> tasks=new ArrayList<>();

    @AssertTrue(message = "startDate doit être avant endDate")
    public boolean isStartDateBeforeEndDate() {
        if (startDate == null || endDate == null) return true; // @NotNull gère les nulls
        return startDate.isBefore(endDate);
    }
    public void addTask(Task task){
        if(!tasks.contains(task)){
            tasks.add(task);
            task.setProject(this);
        }
    }
    public void removeTask(Task task){
        tasks.remove(task);
        task.setProject(null);
    }
}
