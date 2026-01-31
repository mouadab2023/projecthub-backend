package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.sql.Update;

@Data
@Builder
public class ColumnDto {

    @NotNull(groups = Update.class)
    Long id;

    @NotBlank
    private String name;

    @NotNull
    private int position;

    @NotNull
    private Long project;
}
