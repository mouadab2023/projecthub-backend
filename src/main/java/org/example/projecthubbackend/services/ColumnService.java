package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projecthubbackend.dtos.PositionDto;
import org.example.projecthubbackend.dtos.column.ColumnDetailsDto;
import org.example.projecthubbackend.dtos.column.ColumnDto;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.entities.Column;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.mappers.ColumnMapper;
import org.example.projecthubbackend.mappers.TaskMapper;
import org.example.projecthubbackend.repositories.ColumnRepository;
import org.example.projecthubbackend.repositories.ProjectRepository;
import org.example.projecthubbackend.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ColumnService {
    private final ProjectRepository projectRepository;
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    private final ColumnMapper columnMapper;
    private final TaskMapper taskMapper;

    public List<ColumnDto> getAllColumns(Long projectId) {
        List<Column> columns = columnRepository.findAllByProjectIdOrderByPositionDesc(projectId);
        return columns.stream().map(columnMapper::toDTO).collect(Collectors.toList());
    }

    public ColumnDetailsDto findColumnDetailsById(Long projectId, Long id) {
        Column column = columnRepository.findByProjectIdAndId(projectId, id).orElseThrow(() -> new EntityNotFoundException("Column Not Found"));
        List<TaskDto> tasks = taskRepository.findAllByColumnIdOrderByPositionAsc(column.getId()).stream().map(taskMapper::toDTO).toList();
        return ColumnDetailsDto.builder().
                id(column.getId()).
                name(column.getName()).
                position(column.getPosition()).
                tasks(tasks).
                build();
    }

    public ColumnDto createColumn(Long projectId, ColumnDto columnDto) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Project not found"));
        Integer maxPosition = columnRepository.findMaxPosition(projectId);
        int nextPosition = maxPosition == null ? 0 : maxPosition + 1;
        Column column = Column.builder().name(columnDto.getName()).project(project).position(nextPosition).build();
        return columnMapper.toDTO(columnRepository.save(column));
    }

    public ColumnDto updateColumn(Long projectId, Long id, ColumnDto columnDto) {
        Column column = columnRepository.findByProjectIdAndId(projectId, id).orElseThrow(() -> new EntityNotFoundException("Column Not Found"));
        if (column.getName() != null)
            if (!column.getName().equals(columnDto.getName()))
                column.setName(columnDto.getName());
        System.out.println(columnDto.getName());
        return columnMapper.toDTO(columnRepository.save(column));
    }

    public void removeColumn(Long projectId, Long id) {
        Column column = columnRepository.lockColumn(projectId, id).orElseThrow(() -> new EntityNotFoundException("Column Not Found"));
        columnRepository.shiftLeft(projectId,column.getPosition());
        columnRepository.delete(column);
    }

    public void changePosition(Long projectId, Long id, PositionDto dto) {
        Column column = columnRepository.lockColumn(projectId, id)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));

        int oldPosition = column.getPosition();
        int newPosition = dto.getNewPosition();

        int maxPosition = columnRepository.findMaxPosition(projectId);
        if (newPosition < 0 || newPosition > maxPosition) {
            throw new IllegalArgumentException("Invalid position: " + newPosition);
        }

        if (oldPosition == newPosition) return;

        if (oldPosition < newPosition) {
            columnRepository.shiftLeft(projectId, oldPosition, newPosition);
        } else {
            columnRepository.shiftRight(projectId, oldPosition, newPosition);
        }

        column.setPosition(newPosition);
        columnRepository.save(column);
    }
}
