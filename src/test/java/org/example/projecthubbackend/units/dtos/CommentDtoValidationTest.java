package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.CommentDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class CommentDtoValidationTest{
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testCommentNull(){
        CommentDto commentDto = validCommentDto();
        commentDto.setComment(null);
        assertViolation(commentDto,"comment");
    }
    @Test
    public void testCommentBlank(){
        CommentDto commentDto = validCommentDto();
        commentDto.setComment("");
        assertViolation(commentDto,"comment");
    }
    @Test
    public void testCommentedAtNull(){
        CommentDto commentDto = validCommentDto();
        commentDto.setCommentedAt(null);
        assertViolation(commentDto,"commentedAt");
    }
    @Test
    public void testAuthorNull(){
        CommentDto commentDto = validCommentDto();
        commentDto.setAuthor(null);
        assertViolation(commentDto,"author");
    }
    @Test
    public void testTaskNull(){
        CommentDto commentDto = validCommentDto();
        commentDto.setTask(null);
        assertViolation(commentDto,"task");
    }

    public void assertViolation(CommentDto commentDto,String propertyName){
        Set<ConstraintViolation<CommentDto>> violations=validator.validate(commentDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
    public CommentDto validCommentDto(){
        return CommentDto.builder()
                .comment("comment")
                .commentedAt(LocalDateTime.of(
                        LocalDate.of(2020,01,01),
                        LocalTime.of(00,00)
                ))
                .author(1L)
                .task(1L).
                build();
    }
}

