package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.projecthubbackend.dtos.groups.Update;

@Data
@Builder
public class ItemDto {
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private boolean isChecked;

    @NotNull
    private int position;

    @NotNull
    private Long task;
}
