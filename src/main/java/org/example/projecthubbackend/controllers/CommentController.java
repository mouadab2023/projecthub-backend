package org.example.projecthubbackend.controllers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.CommentDto;
import org.example.projecthubbackend.dtos.groups.Create;
import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("projects/{projectId}/columns/{columnId}/tasks/{taskId}/comments")
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllItems(@PathVariable Long projectId,
                                                        @PathVariable Long columnId,
                                                        @PathVariable Long taskId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComments(projectId, columnId, taskId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getItem(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findComment(projectId, columnId, taskId, id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createItem(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @Validated(Create.class) @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(projectId, columnId, taskId, commentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @NotNull @Min(1) @PathVariable Long id,
            @Validated(Update.class) @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(projectId, columnId, taskId, id, commentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long columnId,
            @PathVariable Long taskId,
            @PathVariable @NotNull @Min(1) Long id) {
        commentService.removeComment(projectId, columnId, taskId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
