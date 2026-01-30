package org.example.projecthubbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.projecthubbackend.enumerations.Priority;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Priority priority;

    @Valid
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    private Column column;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;


}