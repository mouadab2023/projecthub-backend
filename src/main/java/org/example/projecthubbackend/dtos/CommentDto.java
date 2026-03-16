package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.groups.Create;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    Long id;

    @NotBlank(groups = Create.class)
    private String comment;

    private LocalDateTime commentedAt;

    private Long author;
}
