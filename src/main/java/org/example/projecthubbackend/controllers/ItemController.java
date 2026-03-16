package org.example.projecthubbackend.controllers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.ItemDto;
import org.example.projecthubbackend.dtos.PositionDto;
import org.example.projecthubbackend.dtos.groups.Create;
import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("projects/{projectId}/columns/{columnId}/tasks/{taskId}/items")
@Validated
public class ItemController {
    final private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(@PathVariable Long projectId,
                                                     @PathVariable Long columnId,
                                                     @PathVariable Long taskId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getAllItems(projectId, columnId, taskId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.findItem(projectId, columnId, taskId, id));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(projectId, columnId, taskId, itemDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @NotNull @Min(1) @PathVariable Long id,
            @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.updateItem(projectId, columnId, taskId, id, itemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @PathVariable @NotNull @Min(1) Long id) {
        itemService.removeItem(projectId, columnId, taskId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("{id}/position")
    public ResponseEntity<Void> changePosition(@PathVariable Long projectId,
                                               @PathVariable Long columnId,
                                               @PathVariable Long taskId,
                                               @PathVariable @NotNull @Min(1) Long id,
                                               @Validated @RequestBody PositionDto newPositionDto) {
        itemService.changePosition(projectId, columnId, taskId, id, newPositionDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
