package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ReadUserDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testFirstNameNull(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setFirstName(null);
        assertViolation(readUserDto,"firstName");
    }
    @Test
    public void testFirstNameBlank(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setFirstName("");
        assertViolation(readUserDto,"firstName");
    }
    @Test
    public void testLastNameNull(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setLastName(null);
        assertViolation(readUserDto,"lastName");
    }
    @Test
    public void testLastNameBlank(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setLastName("");
        assertViolation(readUserDto,"lastName");
    }
    @Test
    public void testEmailNull(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setEmail(null);
        assertViolation(readUserDto,"email");
    }
    @Test
    public void testEmailBlank(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setEmail("");
        assertViolation(readUserDto,"email");
    }
    @Test
    public void testEmailWithoutAt(){
        ReadUserDto readUserDto =validUserDto();
        readUserDto.setEmail("mail.com");
        assertViolation(readUserDto,"email");
    }

    @Test
    public void testValidUserDto(){
        ReadUserDto readUserDto =validUserDto();
        Set<ConstraintViolation<ReadUserDto>> violations=validator.validate(readUserDto);
        Assertions.assertEquals(0,violations.size());
    }
    public ReadUserDto validUserDto(){
        return ReadUserDto.builder().
                id(1L).
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dot.net").
                build();
    }
    public void assertViolation(ReadUserDto readUserDto, String propertyName){
        Set<ConstraintViolation<ReadUserDto>> violations=validator.validate(readUserDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}

