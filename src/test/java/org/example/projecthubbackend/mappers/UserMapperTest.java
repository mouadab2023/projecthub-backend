package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.user.ReadUserDto;
import org.example.projecthubbackend.dtos.user.ReadUserMinDto;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.Priority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void  shouldMapUserToReadUserMinDto() {
        User user=createValidUser();
        ReadUserMinDto readUserMinDto=userMapper.toReadUserMinDto(user);

        Assertions.assertNotNull(readUserMinDto);
        Assertions.assertEquals(user.getId(),readUserMinDto.getId());
        Assertions.assertEquals(user.getFirstName(),readUserMinDto.getFirstName());
        Assertions.assertEquals(user.getLastName(),readUserMinDto.getLastName());
        Assertions.assertEquals(user.getEmail(),readUserMinDto.getEmail());
        Assertions.assertEquals(user.getAvatarUrl(),readUserMinDto.getAvatarUrl());
        Assertions.assertEquals(user.getRoles(),readUserMinDto.getRoles());

    }
    @Test
    public void shouldMapUserToReadUserDto() {
        User user=createValidUser();

        Project project =createValidProject(user);

        Task task1 = createValidTask(1L);
        Task task2 = createValidTask(2L);
        project.addTask(task1);
        project.addTask(task2);

        ReadUserDto readUserDto=userMapper.toDTO(user);
        Assertions.assertNotNull(readUserDto);
        Assertions.assertEquals(user.getId(),readUserDto.getId());
        Assertions.assertEquals(user.getFirstName(),readUserDto.getFirstName());
        Assertions.assertEquals(user.getLastName(),readUserDto.getLastName());
        Assertions.assertEquals(user.getEmail(),readUserDto.getEmail());
        Assertions.assertEquals(user.getAvatarUrl(),readUserDto.getAvatarUrl());
        Assertions.assertEquals(user.getRoles(),readUserDto.getRoles());
        List<Long> entityProjectIds = user.getProjects().stream()
                .map(Project::getId)
                .toList();
        Assertions.assertEquals(entityProjectIds,readUserDto.getProjects());
        List<Long> entityTaskIds = user.getAssignedTasks().stream()
                .map(Task::getId)
                .toList();
        Assertions.assertEquals(entityTaskIds,readUserDto.getAssignedTasks());
    }
    public User createValidUser(){
        return User.builder().
                id(1L).
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                avatarUrl("url").
                password("hash").build();
    }
    public Project createValidProject(User owner){
        return Project.builder().
                id(1L).
                name("project").
                startDate(LocalDate.of(2020,12,12)).
                endDate(LocalDate.of(2099,12,12)).
                owner(owner).
                build();
    }
    public Task createValidTask(Long id){
        User user =  createValidUser();
        Project project = createValidProject(user);
        return Task.builder().
                id(id).
                title("title").
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();
    }
}
