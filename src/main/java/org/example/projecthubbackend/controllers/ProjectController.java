package org.example.projecthubbackend.controllers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.groups.Create;
import org.example.projecthubbackend.dtos.groups.Update;
import org.example.projecthubbackend.dtos.project.ProjectBoardDto;
import org.example.projecthubbackend.dtos.project.ProjectDetailsDto;
import org.example.projecthubbackend.dtos.project.ProjectDto;
import org.example.projecthubbackend.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Validated
public class ProjectController {
    final private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsDto> getProject(@NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findProjectDetailsById(id));
    }

    @GetMapping("/{id}/board")
    public ResponseEntity<ProjectBoardDto> getBoard(@NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getBoard(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Validated(Create.class) @RequestBody ProjectDto projectDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@NotNull @Min(1) @PathVariable Long id, @Validated(Update.class) @RequestBody ProjectDto projectDto) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(id, projectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable @NotNull @Min(1) Long id) {
        projectService.removeProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
