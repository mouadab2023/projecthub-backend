package org.example.projecthubbackend.dtos.column;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projecthubbackend.dtos.groups.Create;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDto {

    Long id;

    @NotNull(groups = Create.class)
    private String name;

    private Integer position;
}
