package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.task.TaskDto;
import org.example.projecthubbackend.enumerations.Priority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

public class TaskDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testTitleNull(){
        TaskDto taskDto = validTaskDto();
        taskDto.setTitle(null);
        assertViolation(taskDto,"title");
    }
    @Test
    public void testTitleBlank(){
        TaskDto taskDto = validTaskDto();
        taskDto.setTitle("");
        assertViolation(taskDto,"title");
    }
    @Test
    public void testDescriptionNull(){
        TaskDto taskDto = validTaskDto();
        taskDto.setDescription(null);
        assertViolation(taskDto,"description");
    }
    @Test
    public void testDescriptionBlank(){
        TaskDto taskDto = validTaskDto();
        taskDto.setDescription("");
        assertViolation(taskDto,"description");
    }
    @Test
    public void testPriorityNull(){
        TaskDto taskDto = validTaskDto();
        taskDto.setPriority(null);
        assertViolation(taskDto,"priority");
    }
    @Test
    public void testProjectNull(){
        TaskDto taskDto = validTaskDto();
        taskDto.setProject(null);
        assertViolation(taskDto,"project");
    }
    public TaskDto validTaskDto() {
        return TaskDto.builder().
                title("title").
                description("description").
                dueDate(LocalDate.of(2020, 01, 01)).
                priority(Priority.LOW).
                project(1L)
                .build();
    }
    public void assertViolation(TaskDto taskDto, String propertyName){
        Set<ConstraintViolation<TaskDto>> violations=validator.validate(taskDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}

