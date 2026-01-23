package org.example.projecthubbackend.mappers;

import org.example.projecthubbackend.dtos.ProjectDto;
import org.example.projecthubbackend.dtos.TaskDto;
import org.example.projecthubbackend.entities.Project;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.entities.User;
import org.example.projecthubbackend.enumerations.Priority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class TaskMapperTest {
    @Autowired
    private TaskMapper taskMapper;
    @Test
    public void shouldMapToTaskDto(){
        Task task = createValidTask();

        TaskDto taskDto = taskMapper.toDTO(task);

        Assertions.assertNotNull(taskDto);
        Assertions.assertEquals(task.getTitle(),taskDto.getTitle());
        Assertions.assertEquals(task.getDescription(),taskDto.getDescription());
        Assertions.assertEquals(task.getDueDate(),taskDto.getDueDate());
        Assertions.assertEquals(task.getPriority(),taskDto.getPriority());
        Assertions.assertEquals(task.getStatus(),taskDto.getStatus());
        Assertions.assertEquals(task.getProject().getId(),taskDto.getProject());

    }
    @Test
    public void shouldMapToEntity(){
        TaskDto taskDto = createValidTaskDto();
        Task task = taskMapper.toEntityBasics(taskDto);
        Assertions.assertNotNull(task);
        Assertions.assertEquals(taskDto.getTitle(),task.getTitle());
        Assertions.assertEquals(taskDto.getDescription(),task.getDescription());
        Assertions.assertEquals(taskDto.getDueDate(),task.getDueDate());
        Assertions.assertEquals(taskDto.getPriority(),task.getPriority());
        Assertions.assertEquals(taskDto.getStatus(),task.getStatus());
    }
    public TaskDto createValidTaskDto(){
       return TaskDto.builder()
               .id(1L)
               .title("title")
               .description("description")
               .dueDate(LocalDate.now())
               .priority(Priority.LOW)
               .project(1L)
               .build();
    }
    public User createValidUser(){
        return User.builder().
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
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
    public Task createValidTask(){
        User user =  createValidUser();
        Project project = createValidProject(user);
        return Task.builder().
                title("title").
                description("description").
                dueDate(LocalDate.now()).
                priority(Priority.LOW).
                project(project).
                build();
    }
}