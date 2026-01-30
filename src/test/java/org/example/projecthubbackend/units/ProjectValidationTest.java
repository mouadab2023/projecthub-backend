package org.example.projecthubbackend.units;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

public class ProjectValidationTest
{
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testNameNull(){
        User owner = createValidUser();
        Project project =Project.builder().
                name(null).
                creationDate(LocalDate.of(2020,12,12)).
                owner(owner).
                build();
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("name")));
    }
    @Test
    public void testNameBlank(){
        User owner = createValidUser();
        Project project =Project.builder().
                name("").
                creationDate(LocalDate.of(2020,12,12)).
                owner(owner).
                build();
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("name")));
    }
    @Test
    public void testCreationDateNull(){
        User owner = createValidUser();
        Project project =Project.builder().
                name("name").
                creationDate(null).
                owner(owner).
                build();
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("creationDate")));
    }
    @Test
    public void testOwnerNull(){
        Project project =Project.builder().
                name("name").
                creationDate(LocalDate.of(2020,12,12)).
                owner(null).
                build();
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("owner")));
    }
    @Test
    public void testAddTask(){
        Task task = new Task();
        Project project = new Project();

        project.addTask(task);

        Assertions.assertEquals(1,project.getTasks().size());
        Assertions.assertTrue(project.getTasks().contains(task));
    }
    @Test
    public void testRemoveTask(){
        Task task = new Task();
        Project project = new Project();
        project.addTask(task);

        Assertions.assertEquals(1,project.getTasks().size());
        Assertions.assertTrue(project.getTasks().contains(task));

        project.removeTask(task);

        Assertions.assertEquals(0,project.getTasks().size());
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
}

