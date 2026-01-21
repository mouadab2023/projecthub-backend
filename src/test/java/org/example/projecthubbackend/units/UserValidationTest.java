package org.example.projecthubbackend.units;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setup(){
        validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void testFirstNameNull(){
        User user =  User.builder().
                firstName(null).
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().
                anyMatch(violation -> violation.getPropertyPath().toString().equals("firstName")));
    }
    @Test
    public void testFirstNameBlank(){
        User user =  User.builder().
                firstName("").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("firstName")));
    }
    @Test
    public void testLastNameNull(){
        User user =  User.builder().
                firstName("alex").
                lastName(null).
                email("alexdupont@dom.fr").
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("lastName")));
    }
    @Test
    public void testLastNameBlank(){
        User user =  User.builder().
                firstName("alex").
                lastName("").
                email("alexdupont@dom.fr").
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("lastName")));
    }
    @Test
    public void testEmailNull(){
        User user =  User.builder().
                firstName("alex").
                lastName("dupont").
                email(null).
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("email")));
    }
    @Test
    public void testEmailBlank(){
        User user =  User.builder().
                firstName("alex").
                lastName("dupont").
                email("").
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("email")));
    }
    @Test
    public void testEmailWithoutAt(){
        User user =  User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupontdom.fr").
                password("hash").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("email")));
    }
    @Test
    public void testPasswordNull(){
        User user =  User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password(null).
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("password")));
    }
    @Test
    public void testPasswordBlank(){
        User user =  User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("").
                build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("password")));
    }
    @Test
    public void testRoleNull(){
        User user =  User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("hash").
                roles(null).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        System.out.println(violations.toString());
        assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("roles")));
    }
    @Test
    public void testAddProject(){
        User user = new User();
        Project project= new Project();

        user.addProject(project);

        assertEquals(1,user.getProjects().size());
        assertTrue(user.getProjects().contains(project));
    }
    @Test
    public void testRemoveProject(){
        User user = new User();
        Project project = new Project();

        user.addProject(project);
        user.removeProject(project);

        assertEquals(0,user.getProjects().size());
    }
    @Test
    public void testAddAssignedTask(){
        User user = new User();
        Task task = new Task();

        user.addAssignedTask(task);

        assertEquals(1,user.getAssignedTasks().size());
        assertTrue(user.getAssignedTasks().contains(task));
    }
    @Test
    public void testRemoveAssignedTask(){
        User user = new User();
        Task task = new Task();

        user.addAssignedTask(task);
        user.removeAssignedTask(task);

        assertEquals(0,user.getAssignedTasks().size());
    }
}

