package org.example.projecthubbackend.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.task.TaskDetailsDto;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.dtos.project.ProjectDto;
import org.example.projecthubbackend.dtos.task.UpdateTaskDto;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Validated
public class TaskController {
    final private TaskService taskService;

    @GetMapping("")
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam(required = false) Long  projectId ,@RequestParam(required = false) Long  ColumnId){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasksBy(projectId,ColumnId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailsDto> getTask(@NotNull @Min(1) @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findTaskDetailsById(id));
    }
    @PostMapping("/")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@NotNull @Min(1) @PathVariable Long id, @Valid @RequestBody TaskDto taskDto){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(id,taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @NotNull @Min(1) Long id){
        taskService.removeTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
