package org.example.projecthubbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String comment;

    @NotNull
    private LocalDateTime commentedAt;

    @Valid
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectMember author;

    @Valid
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

}


