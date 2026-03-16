package org.example.projecthubbackend.controllers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projecthubbackend.dtos.MoveTaskDto;
import org.example.projecthubbackend.dtos.groups.Create;
import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.dtos.task.TaskDetailsDto;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("projects/{projectId}/columns/{columnId}/tasks")
@Validated
public class TaskController {
    final private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable Long projectId, @PathVariable Long columnId) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks(projectId, columnId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailsDto> getTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findTaskDetails(projectId, columnId, id));
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @Validated(Create.class) @RequestBody TaskDto taskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(projectId, columnId, taskDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @NotNull @Min(1) @PathVariable Long id,
            @Validated(Update.class) @RequestBody TaskDto taskDto) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(projectId, columnId, id, taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable @NotNull @Min(1) Long id) {
        taskService.removeTask(projectId, columnId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<TaskDto> changePosition(@PathVariable Long projectId,
                                               @PathVariable Long columnId,
                                               @PathVariable @NotNull @Min(1) Long id,
                                               @Validated @RequestBody MoveTaskDto moveTaskDto) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.moveTask(projectId, columnId, id, moveTaskDto));
    }
}
