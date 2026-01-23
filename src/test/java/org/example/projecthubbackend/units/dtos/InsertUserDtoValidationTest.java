package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.user.InsertUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class InsertUserDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testFirstNameNull(){
        InsertUserDto userDto=validUserDto();
        userDto.setFirstName(null);
        assertViolation(userDto,"firstName");
    }
    @Test
    public void testFirstNameBlank(){
        InsertUserDto userDto=validUserDto();
        userDto.setFirstName("");
        assertViolation(userDto,"firstName");
    }
    @Test
    public void testLastNameNull(){
        InsertUserDto userDto=validUserDto();
        userDto.setLastName(null);
        assertViolation(userDto,"lastName");
    }
    @Test
    public void testLastNameBlank(){
        InsertUserDto userDto=validUserDto();
        userDto.setLastName("");
        assertViolation(userDto,"lastName");
    }
    @Test
    public void testEmailNull(){
        InsertUserDto userDto=validUserDto();
        userDto.setEmail(null);
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailBlank(){
        InsertUserDto userDto=validUserDto();
        userDto.setEmail("");
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailWithoutAt(){
        InsertUserDto userDto=validUserDto();
        userDto.setEmail("mail.com");
        assertViolation(userDto,"email");
    }

    @Test
    public void testPasswordNull(){
        InsertUserDto userDto=validUserDto();
        userDto.setPassword(null);
        assertViolation(userDto,"password");
    }
    @Test
    public void testPasswordEmpty(){
        InsertUserDto userDto=validUserDto();
        userDto.setPassword("");
        assertViolation(userDto,"password");
    }
    @Test
    public void testPasswordShorterThanMinSize(){
        InsertUserDto userDto=validUserDto();
        userDto.setPassword("Pp22$");
        assertViolation(userDto,"password");
    }
    @Test
    public void testWeakPassword(){
        InsertUserDto userDto=validUserDto();
        userDto.setPassword("passwordpassword");
        assertViolation(userDto,"password");
    }

    @Test
    public void testValidUserDto(){
        InsertUserDto userDto=validUserDto();
        Set<ConstraintViolation<InsertUserDto>> violations=validator.validate(userDto);
        Assertions.assertEquals(0,violations.size());
    }
    public InsertUserDto validUserDto(){
        return InsertUserDto.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dot.net").
                password("Ppassword2026@").
                build();
    }
    public void assertViolation(InsertUserDto userDto, String propertyName){
        Set<ConstraintViolation<InsertUserDto>> violations=validator.validate(userDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}
