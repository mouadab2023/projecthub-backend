package org.example.projecthubbackend.units.dtos;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.project.ProjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

public class ProjectDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testNameNull(){
        ProjectDto projectDto = validProjectDTO();
        projectDto.setName(null);
        assertViolation(projectDto,"name");
    }
    @Test
    public void testNameBlank(){
        ProjectDto projectDto = validProjectDTO();
        projectDto.setName("");
        assertViolation(projectDto,"name");
    }
    @Test
    public void testCreationDateNull(){
        ProjectDto projectDto = validProjectDTO();
        projectDto.setCreationDate(null);
        assertViolation(projectDto,"creationDate");
    }

    @Test
    public void testOwnerNull(){
        ProjectDto projectDto = validProjectDTO();
        projectDto.setOwner(null);
        assertViolation(projectDto,"owner");
    }
    public ProjectDto validProjectDTO(){
        return ProjectDto.builder().
                name("name").
                creationDate(LocalDate.of(2020,01,01)).
                owner(1L).
                build();
    }
    public void assertViolation(ProjectDto projectDto, String propertyName){
        Set<ConstraintViolation<ProjectDto>> violations=validator.validate(projectDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}
