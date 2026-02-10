package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.task.TaskDetailsDto;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.entities.*;
import org.example.projecthubbackend.mappers.ItemMapper;
import org.example.projecthubbackend.mappers.TaskMapper;
import org.example.projecthubbackend.repositories.*;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ColumnRepository columnRepository;
    private final TaskMapper taskMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    private final AuthenticationService authenticationService;
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final CommentRepository commentRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.isProjectMember(#projectId)")
    public  List<TaskDto> getAllTasksBy(Long projectId, Long columnId) {
        List<Task> tasks=null;
        if(projectId != null && columnId != null) {
            tasks= taskRepository.findAllByProjectIdAndColumnId(projectId,columnId);
        }else if(projectId == null && columnId == null) {
            tasks= taskRepository.findAll();
        }else if(projectId != null) {
           tasks= taskRepository.findAllByProjectId(projectId);
        }else {
           tasks= taskRepository.findAllByColumnId(columnId);
       }
        return tasks.stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canViewTask(#id)")
    public TaskDetailsDto findTaskDetailsById(@NotNull @Min(1) Long id) {
        Task task=taskRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Task not found"));
        List<Item> items=itemRepository.findAllByTaskId(id);
        return TaskDetailsDto.builder().
                title(task.getTitle()).
                description(task.getDescription()).
                dueDate(task.getDueDate()).
                priority(task.getPriority()).
                project(task.getProject().getId()).
                column(task.getColumn().getId()).
                items(items.stream().map(itemMapper::toDTO).collect(Collectors.toList())).
                build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canCreateTask(#taskDto.getId())")
    public TaskDto createTask(@Valid TaskDto taskDto) {
        Project project=projectRepository.findById(taskDto.getProject()).orElseThrow(()->new EntityNotFoundException("Project not found"));
        Column column=columnRepository.findById(taskDto.getColumn()).orElseThrow(()->new EntityNotFoundException("Column not found"));
        Task task = Task.builder().
                title(taskDto.getTitle()).
                description(taskDto.getDescription()).
                dueDate(taskDto.getDueDate()).
                priority(taskDto.getPriority()).
                project(project).
                column(column).
                project(project).
                build();
        Task savedTask =taskRepository.save(task);

        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        TaskAssignee taskAssignee=TaskAssignee.builder().
                task(savedTask).
                user(currentUser).
                assignedAt(LocalDateTime.now()).
                build();
        taskAssigneeRepository.save(taskAssignee);

        return taskMapper.toDTO(savedTask);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canEditTask(#taskDto.getId())")
    public  TaskDto updateTask(@NotNull @Min(1) Long id, @Valid TaskDto taskDto) {
        Task task=taskRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Task not found"));
        if (!taskDto.getTitle().equals(task.getTitle()))
            task.setTitle(taskDto.getTitle());
        if (!taskDto.getDescription().equals(task.getDescription()))
            task.setDescription(taskDto.getDescription());
        if (!taskDto.getDueDate().equals(task.getDueDate()))
            task.setDueDate(taskDto.getDueDate());
        if (!taskDto.getPriority().equals(task.getPriority()))
            task.setPriority(taskDto.getPriority());

        return taskMapper.toDTO(taskRepository.save(task));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canDeleteTask(#id)")
    public void removeTask(@NotNull @Min(1) Long id) {
        commentRepository.deleteAllByTaskId(id);
        itemRepository.deleteAllByTaskId(id);
        taskRepository.deleteById(id);
    }
}
