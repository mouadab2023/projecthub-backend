package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.CommentDto;
import org.example.projecthubbackend.entities.Comment;
import org.example.projecthubbackend.entities.ProjectMember;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.mappers.CommentMapper;
import org.example.projecthubbackend.repositories.CommentRepository;
import org.example.projecthubbackend.repositories.ProjectMemberRepository;
import org.example.projecthubbackend.repositories.TaskRepository;
import org.example.projecthubbackend.services.auth.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final ProjectMemberRepository projectMemberRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final AuthenticationService authenticationService;

    public List<CommentDto> getAllComments(Long projectId, Long columnId, Long taskId) {
        List<Comment> comments = commentRepository.findAllByTaskColumnProjectIdAndTaskColumnIdAndTaskId(projectId, columnId, taskId);
        return comments.stream().map(commentMapper::toDTO).collect(Collectors.toList());
    }

    public CommentDto findComment(Long projectId, Long columnId, Long taskId, Long id) {
        Comment comment = commentRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Comment Not Found"));
        return commentMapper.toDTO(comment);
    }

    public CommentDto createComment(Long projectId, Long columnId, Long taskId, CommentDto commentDto) {
        Task task = taskRepository.findByProjectIdAndColumnIdAndId(projectId, columnId, taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        User currentUser = authenticationService.getCurrentUserFromSecurityContext();
        ProjectMember author = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("Project Member Not Found"));
        Comment comment = Comment.builder().
                comment(commentDto.getComment()).
                commentedAt(LocalDateTime.now()).
                task(task).
                author(author).
                build();
        return commentMapper.toDTO(commentRepository.save(comment));
    }

    public CommentDto updateComment(Long projectId, Long columnId, Long taskId, Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Comment Not Found"));
        if (commentDto.getComment() != null)
            if (!commentDto.getComment().equals(comment.getComment()))
                comment.setComment(commentDto.getComment());
        return commentMapper.toDTO(commentRepository.save(comment));
    }

    public void removeComment(
            Long projectId,
            Long columnId,
            Long taskId,
            Long id) {
        commentRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        commentRepository.deleteById(id);
    }
}
