package org.example.projecthubbackend.units;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.entities.Comment;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.Priority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class CommentValidationTest {
    static Validator validator ;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testCommentNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);
        Task task= createValidTask(project);

        Comment comment = Comment.builder()
                .comment(null)
                .commentedAt(LocalDateTime.
                        of(LocalDate.of(2020,01,01), LocalTime.of(14,00)))
                .author(user).
                task(task)
                .build();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        Assertions.assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath().toString().equals("comment")
        ));
    }
    @Test
    public void testCommentBlank(){
        User user =  createValidUser();
        Project project = createValidProject(user);
        Task task= createValidTask(project);

        Comment comment = Comment.builder()
                .comment("")
                .commentedAt(LocalDateTime.
                        of(LocalDate.of(2020,01,01), LocalTime.of(14,00)))
                .author(user).
                task(task)
                .build();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        Assertions.assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath().toString().equals("comment")
        ));
    }
    @Test
    public void testCommentedAtNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);
        Task task= createValidTask(project);

        Comment comment = Comment.builder()
                .comment("comment")
                .commentedAt(null)
                .author(user).
                task(task)
                .build();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        Assertions.assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath().toString().equals("commentedAt")
        ));
    }
    @Test
    public void testAuthorNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);
        Task task= createValidTask(project);

        Comment comment = Comment.builder()
                .comment("comment")
                .commentedAt(LocalDateTime.
                        of(LocalDate.of(2020,01,01), LocalTime.of(14,00)))
                .author(null).
                task(task)
                .build();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        Assertions.assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath().toString().equals("author")
        ));
    }
    @Test
    public void testTaskNull(){
        User user =  createValidUser();
        Comment comment = Comment.builder()
                .comment("comment")
                .commentedAt(LocalDateTime.
                        of(LocalDate.of(2020,01,01), LocalTime.of(14,00)))
                .author(user).
                task(null)
                .build();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        Assertions.assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath().toString().equals("task")
        ));
    }
    // Helpers
    public User createValidUser(){
        return User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("hash").
                build();
    }
    public Project createValidProject(User owner){
        return Project.builder().
                name("project").
                startDate(LocalDate.of(2020,12,12)).
                endDate(LocalDate.of(2099,12,12)).
                owner(owner).
                build();
    }
    public Task createValidTask(Project project){
        return Task.builder().
                title("title").
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();
    }
}

