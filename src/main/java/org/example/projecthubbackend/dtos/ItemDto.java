package org.example.projecthubbackend.dtos;

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
public class ItemDto {
    private Long id;

    @NotNull(groups = Create.class)
    private String name;

    @NotNull(groups = Create.class)
    private Boolean isChecked;

    private Integer position;

}
