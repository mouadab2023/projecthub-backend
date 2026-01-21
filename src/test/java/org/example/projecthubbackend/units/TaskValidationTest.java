package org.example.projecthubbackend.units;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.Priority;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testTitleNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);

        Task task = Task.builder().
                title(null).
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }
    @Test
    public void testTitleBlank(){
        User user =  createValidUser();
        Project project = createValidProject(user);

        Task task = Task.builder().
                title("").
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }
    @Test
    public void testDescriptionNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);

        Task task = Task.builder().
                title("title").
                description(null).
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }
    @Test
    public void testDescriptionBlank(){
        User user =  createValidUser();
        Project project = createValidProject(user);

        Task task = Task.builder().
                title("title").
                description("").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }
    @Test
    public void testDueDateNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);

        Task task = Task.builder().
                title("title").
                description("description").
                dueDate(null).
                priority(Priority.LOW).
                project(project).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dueDate")));
    }
    @Test
    public void testPriorityNull(){
        User user =  createValidUser();
        Project project = createValidProject(user);

        Task task = Task.builder().
                title("title").
                description("description").
                dueDate(LocalDate.now()).
                priority(null).
                project(project).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("priority")));
    }
    @Test
    public void testProjectNull(){
        Task task = Task.builder().
                title("title").
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(null).
                build();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("project")));
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
}

