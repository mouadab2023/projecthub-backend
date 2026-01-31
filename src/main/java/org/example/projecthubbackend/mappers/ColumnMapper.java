package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.ColumnDto;
import org.example.projecthubbackend.entities.Column;
import org.springframework.stereotype.Component;

@Component
public class ColumnMapper {
    public ColumnDto toDTO(Column column) {
        return ColumnDto.builder()
                .id(column.getId())
                .name(column.getName())
                .position(column.getPosition())
                .project(column.getProject()!=null?column.getProject().getId():null)
                .build();
    }
}
