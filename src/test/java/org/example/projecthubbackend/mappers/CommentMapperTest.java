package org.example.projecthubbackend.mappers;

import jakarta.transaction.Transactional;
import org.example.projecthubbackend.dtos.CommentDto;
import org.example.projecthubbackend.entities.Comment;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.Priority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;
    @Test
    public void  shouldMapCommentToCommentDto(){
        Comment comment=createValidComment();
        CommentDto commentDto=commentMapper.toDTO(comment);

        Assertions.assertNotNull(commentDto);
        Assertions.assertEquals(comment.getId(),commentDto.getId());
        Assertions.assertEquals(comment.getComment(),commentDto.getComment());
        Assertions.assertEquals(comment.getTask().getId(),commentDto.getTask());
        Assertions.assertEquals(comment.getAuthor().getId(),commentDto.getAuthor());
        Assertions.assertEquals(comment.getCommentedAt(),commentDto.getCommentedAt());

    }
    @Test
    public void  shouldMapCommentDtoToComment(){
    CommentDto commentDto=createValidCommentDto();
    Comment comment=commentMapper.toEntityBasics(commentDto);
    Assertions.assertNotNull(comment);
    Assertions.assertEquals(commentDto.getId(),comment.getId());
    Assertions.assertEquals(commentDto.getComment(),comment.getComment());
    Assertions.assertEquals(commentDto.getCommentedAt(),comment.getCommentedAt());
    }

    public CommentDto createValidCommentDto(){
        return CommentDto.builder()
                .id(1L)
                .comment("comment")
                .task(1L)
                .author(1L)
                .commentedAt(LocalDateTime.now())
                .build();
    }
    public Comment createValidComment(){
        Task task=createValidTask();
        User author=createValidUser();
        return Comment.builder()
                .id(1L)
                .task(task)
                .comment("comment")
                .author(author)
                .commentedAt(LocalDateTime.now())
                .build();
    }

    public User createValidUser(){
        return User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("hash").build();
    }
    public Project createValidProject(User owner){
        return Project.builder().
                id(1L).
                name("project").
                creationDate(LocalDate.of(2020,12,12)).
                owner(owner).
                build();
    }
    public Task createValidTask(){
        User user =  createValidUser();
        Project project = createValidProject(user);
        return Task.builder().
                title("title").
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();
    }
    }
