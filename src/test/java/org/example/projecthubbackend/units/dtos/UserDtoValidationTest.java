package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UserDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testFirstNameNull(){
        UserDto userDto=validUserDto();
        userDto.setFirstName(null);
        assertViolation(userDto,"firstName");
    }
    @Test
    public void testFirstNameBlank(){
        UserDto userDto=validUserDto();
        userDto.setFirstName("");
        assertViolation(userDto,"firstName");
    }
    @Test
    public void testLastNameNull(){
        UserDto userDto=validUserDto();
        userDto.setLastName(null);
        assertViolation(userDto,"lastName");
    }
    @Test
    public void testLastNameBlank(){
        UserDto userDto=validUserDto();
        userDto.setLastName("");
        assertViolation(userDto,"lastName");
    }
    @Test
    public void testEmailNull(){
        UserDto userDto=validUserDto();
        userDto.setEmail(null);
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailBlank(){
        UserDto userDto=validUserDto();
        userDto.setEmail("");
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailWithoutAt(){
        UserDto userDto=validUserDto();
        userDto.setEmail("mail.com");
        assertViolation(userDto,"email");
    }

    @Test
    public void testValidUserDto(){
        UserDto userDto=validUserDto();
        Set<ConstraintViolation<UserDto>> violations=validator.validate(userDto);
        Assertions.assertEquals(0,violations.size());
    }
    public UserDto validUserDto(){
        return UserDto.builder().
                id(1L).
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dot.net").
                build();
    }
    public void assertViolation(UserDto userDto,String propertyName){
        Set<ConstraintViolation<UserDto>> violations=validator.validate(userDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}

