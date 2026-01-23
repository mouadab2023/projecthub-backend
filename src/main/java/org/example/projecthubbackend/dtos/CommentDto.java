package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.sql.Update;

import java.time.LocalDateTime;

@Data
@Builder

public class CommentDto {
    @NotNull(groups = Update.class)
    Long id;

    @NotBlank
    private String comment;

    @NotNull
    private LocalDateTime commentedAt;

    @NotNull
    private Long author;

    @NotNull
    private Long task;
}
