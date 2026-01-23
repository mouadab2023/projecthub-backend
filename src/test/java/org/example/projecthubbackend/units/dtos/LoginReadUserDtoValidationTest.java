package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.user.LoginUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class LoginReadUserDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testEmailNull(){
        LoginUserDto userDto=validUserDto();
        userDto.setEmail(null);
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailBlank(){
        LoginUserDto userDto=validUserDto();
        userDto.setEmail("");
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailWithoutAt(){
        LoginUserDto userDto=validUserDto();
        userDto.setEmail("mail.com");
        assertViolation(userDto,"email");
    }

    @Test
    public void testValidUserDto(){
        LoginUserDto userDto=validUserDto();
        Set<ConstraintViolation<LoginUserDto>> violations=validator.validate(userDto);
        Assertions.assertEquals(0,violations.size());
    }
    public LoginUserDto validUserDto(){
        return LoginUserDto.builder().
                email("alexdupont@dot.net").
                password("12345").

                build();
    }
    public void assertViolation(LoginUserDto userDto,String propertyName){
        Set<ConstraintViolation<LoginUserDto>> violations=validator.validate(userDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}
