package org.example.projecthubbackend.units.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.dtos.user.UpdateUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UpdateReadUserDtoValidationTest {
    private static Validator validator;
    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testFirstNameNull(){
        UpdateUserDto userDto=validUserDto();
        userDto.setFirstName(null);
        assertViolation(userDto,"firstName");
    }
    @Test
    public void testFirstNameBlank(){
        UpdateUserDto userDto=validUserDto();
        userDto.setFirstName("");
        assertViolation(userDto,"firstName");
    }
    @Test
    public void testLastNameNull(){
        UpdateUserDto userDto=validUserDto();
        userDto.setLastName(null);
        assertViolation(userDto,"lastName");
    }
    @Test
    public void testLastNameBlank(){
        UpdateUserDto userDto=validUserDto();
        userDto.setLastName("");
        assertViolation(userDto,"lastName");
    }
    @Test
    public void testEmailNull(){
        UpdateUserDto userDto=validUserDto();
        userDto.setEmail(null);
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailBlank(){
        UpdateUserDto userDto=validUserDto();
        userDto.setEmail("");
        assertViolation(userDto,"email");
    }
    @Test
    public void testEmailWithoutAt(){
        UpdateUserDto userDto=validUserDto();
        userDto.setEmail("mail.com");
        assertViolation(userDto,"email");
    }

    @Test
    public void testValidUserDto(){
        UpdateUserDto userDto=validUserDto();
        Set<ConstraintViolation<UpdateUserDto>> violations=validator.validate(userDto);
        Assertions.assertEquals(0,violations.size());
    }
    public UpdateUserDto validUserDto(){
        return UpdateUserDto.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dot.net").
                build();
    }
    public void assertViolation(UpdateUserDto userDto,String propertyName){
        Set<ConstraintViolation<UpdateUserDto>> violations=validator.validate(userDto);
        Assertions.assertTrue(violations.stream().anyMatch(violation->violation.getPropertyPath().toString().equals(propertyName)));
    }
}
