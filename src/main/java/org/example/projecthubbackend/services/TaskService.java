package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projecthubbackend.dtos.MoveTaskDto;
import org.example.projecthubbackend.dtos.task.TaskDetailsDto;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.entities.*;
import org.example.projecthubbackend.mappers.CommentMapper;
import org.example.projecthubbackend.mappers.ItemMapper;
import org.example.projecthubbackend.mappers.TaskAssigneeMapper;
import org.example.projecthubbackend.mappers.TaskMapper;
import org.example.projecthubbackend.repositories.*;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
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
    private final CommentMapper commentMapper;
    private final TaskAssigneeMapper taskAssigneeMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.isProjectMember(#projectId)")
    public List<TaskDto> getAllTasks(Long projectId, Long columnId) {
        List<Task> tasks = taskRepository.findAllByProjectIdAndColumnId(projectId, columnId);
        return tasks.stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canViewTask(#id)")
    public TaskDetailsDto findTaskDetails(Long projectId, Long columnId, Long id) {
        Task task = taskRepository.findByProjectIdAndColumnIdAndId(projectId, columnId, id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        List<Item> items = itemRepository.findAllByTaskColumnProjectIdAndTaskColumnIdAndTaskId(projectId, columnId, id);
        List<Comment> comments = commentRepository.findAllByTaskColumnProjectIdAndTaskColumnIdAndTaskId(projectId, columnId, id);
        List<TaskAssignee> taskAssignees = taskAssigneeRepository.findAllByTask(task);
        return TaskDetailsDto.builder().
                id(task.getId()).
                title(task.getTitle()).
                description(task.getDescription()).
                dueDate(task.getDueDate()).
                priority(task.getPriority()).
                project(projectId).
                column(columnId).
                items(items.stream().map(itemMapper::toDTO).collect(Collectors.toList())).
                comments(comments.stream().map(commentMapper::toDTO).collect(Collectors.toList())).
                assignees(taskAssignees.stream().map(taskAssigneeMapper::toDTO).collect(Collectors.toList())).
                build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canCreateTask(#taskDto.getId())")
    public TaskDto createTask(Long projectId, Long ColumnId, TaskDto taskDto) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Project not found"));
        Column column = columnRepository.findById(ColumnId).orElseThrow(() -> new EntityNotFoundException("Column not found"));
        Integer maxPosition =taskRepository.findMaxPosition(projectId,ColumnId);
        Task task = Task.builder().
                title(taskDto.getTitle()).
                description(taskDto.getDescription()).
                dueDate(taskDto.getDueDate()).
                priority(taskDto.getPriority()).
                position(maxPosition==null?0:maxPosition+1).
                project(project).
                column(column).
                build();
        Task savedTask = taskRepository.save(task);

        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        TaskAssignee taskAssignee = TaskAssignee.builder().
                task(savedTask).
                user(currentUser).
                assignedAt(LocalDateTime.now()).
                build();
        taskAssigneeRepository.save(taskAssignee);

        return taskMapper.toDTO(savedTask);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canEditTask(#taskDto.getId())")
    public TaskDto updateTask(Long projectId,
                              Long columnId,
                              Long id,
                              TaskDto taskDto) {
        Task task = taskRepository.findByProjectIdAndColumnIdAndId(projectId, columnId, id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (taskDto.getTitle() != null)
            if (!taskDto.getTitle().equals(task.getTitle()))
                task.setTitle(taskDto.getTitle());
        if (taskDto.getDescription() != null)
            if (!taskDto.getDescription().equals(task.getDescription()))
                task.setDescription(taskDto.getDescription());
        if (taskDto.getDueDate() != null)
            if (!taskDto.getDueDate().equals(task.getDueDate()))
                task.setDueDate(taskDto.getDueDate());
        if (taskDto.getPriority() != null)
            if (!taskDto.getPriority().equals(task.getPriority()))
                task.setPriority(taskDto.getPriority());

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectSecurity.canDeleteTask(#id)")
    public void removeTask(
            Long projectId,
            Long columnId,
            Long id) {
        Task task = taskRepository.lockTask(projectId, columnId, id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        taskRepository.shiftLeft(projectId,columnId,task.getPosition());
        taskRepository.delete(task);
    }

    @Transactional
    public TaskDto moveTask(Long projectId,
                         Long columnId,
                         @NotNull @Min(1) Long taskId,
                         MoveTaskDto moveTaskDto) {
        System.out.println(projectId+' '+columnId+' '+taskId);
        Task task = taskRepository.findByProjectIdAndColumnIdAndId(projectId, columnId, taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));

        Long oldColumnId = task.getColumn().getId();
        int oldPosition = task.getPosition();

        Long targetColumnId = moveTaskDto.getTargetColumnId();
        int newPosition = moveTaskDto.getNewPosition();

        Long firstLockId = Math.min(oldColumnId, targetColumnId);
        Long secondLockId = Math.max(oldColumnId, targetColumnId);

        columnRepository.lockColumn(projectId, firstLockId).orElseThrow(() -> new EntityNotFoundException("Column not found"));
        if (!firstLockId.equals(secondLockId))
            columnRepository.lockColumn(projectId, secondLockId).orElseThrow(() -> new EntityNotFoundException("Column not found"));

        Column targetColumn = columnRepository.findByProjectIdAndId(projectId, targetColumnId).orElseThrow(() -> new EntityNotFoundException("Target column not found"));

        Integer maxPosition = taskRepository.findMaxPosition(projectId, targetColumnId);
        if (newPosition < 0 ) {
            throw new IllegalArgumentException("Invalid position: " + newPosition);
        }
        if (oldPosition == newPosition && Objects.equals(oldColumnId, targetColumnId)) {
            return taskMapper.toDTO(task) ;
        }
        if (Objects.equals(oldColumnId, targetColumnId)) {
            if (oldPosition < newPosition) {
                taskRepository.shiftLeft(projectId, oldColumnId, oldPosition, newPosition);
            } else {
                taskRepository.shiftRight(projectId, oldColumnId, oldPosition, newPosition);
            }
        }
        else {
            taskRepository.shiftLeft(projectId, oldColumnId, oldPosition);
            taskRepository.shiftRight(projectId, targetColumnId, newPosition);
            task.setColumn(targetColumn);
        }

        task.setPosition(newPosition);
        return taskMapper.toDTO(taskRepository.save(task));
    }

}
