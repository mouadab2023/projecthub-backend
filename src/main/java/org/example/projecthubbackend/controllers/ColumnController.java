package org.example.projecthubbackend.controllers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projecthubbackend.dtos.PositionDto;
import org.example.projecthubbackend.dtos.column.ColumnDetailsDto;
import org.example.projecthubbackend.dtos.column.ColumnDto;
import org.example.projecthubbackend.dtos.groups.Create;
import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.services.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/columns")
@Validated
public class ColumnController {
    final private ColumnService columnService;

    @GetMapping
    public ResponseEntity<List<ColumnDto>> getAllColumns(@PathVariable Long projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(columnService.getAllColumns(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColumnDetailsDto> getColumn(@PathVariable Long projectId, @NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(columnService.findColumnDetailsById(projectId, id));
    }

    @PostMapping
    public ResponseEntity<ColumnDto> createColumn(@PathVariable Long projectId, @Validated(Create.class) @RequestBody ColumnDto columnDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(columnService.createColumn(projectId, columnDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColumnDto> updateColumn(@PathVariable Long projectId,
                                                  @NotNull @Min(1) @PathVariable Long id,
                                                  @Validated(Update.class) @RequestBody ColumnDto columnDto) {
        return ResponseEntity.status(HttpStatus.OK).body(columnService.updateColumn(projectId, id, columnDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long projectId,
                                             @PathVariable @NotNull @Min(1) Long id) {
        columnService.removeColumn(projectId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("{id}/position")
    public ResponseEntity<Void> changePosition(@PathVariable Long projectId,
                                               @PathVariable @NotNull @Min(1) Long id,
                                               @Validated @RequestBody PositionDto newPositionDto) {
        columnService.changePosition(projectId, id, newPositionDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
