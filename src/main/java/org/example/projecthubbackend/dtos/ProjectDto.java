package org.example.projecthubbackend.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @NotNull(groups = Update.class)
    Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Long owner;

    private List<Long> tasks;

    @AssertTrue(message = "startDate doit être avant endDate")
    public boolean isStartDateBeforeEndDate() {
        if (startDate == null || endDate == null) return true;
        return startDate.isBefore(endDate);
    }
}

