package org.example.projecthubbackend.mappers;

import jakarta.transaction.Transactional;
import org.example.projecthubbackend.dtos.ProjectDto;
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
@Transactional
public class ProjectMapperTest {

    @Autowired
    private  ProjectMapper projectMapper;

    @Test
    public void  shouldMapProjectToProjectDto(){
        User owner = createValidUser();

        Project project =createValidProject(owner);

         Task task1 = createValidTask(1L);
         Task task2 = createValidTask(2L);
         project.addTask(task1);
         project.addTask(task2);

        ProjectDto projectDto = projectMapper.toDTO(project);

        Assertions.assertNotNull(projectDto);
        Assertions.assertEquals(project.getName(),projectDto.getName());
        Assertions.assertEquals(project.getStartDate(),projectDto.getStartDate());
        Assertions.assertEquals(project.getEndDate(),projectDto.getEndDate());
        Assertions.assertEquals(project.getOwner().getId(),projectDto.getOwner());
        List<Long> dtoTaskIds = projectDto.getTasks();
        List<Long> entityTaskIds = project.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        Assertions.assertEquals( entityTaskIds, dtoTaskIds,"Les IDs des tasks ne correspondent pas");
    }
    @Test
    public void  shouldMapProjectDtoToProject(){
     ProjectDto projectDto=createValidProjectDto();
     Project project=projectMapper.toEntityBasics(projectDto);
        Assertions.assertNotNull(project);
        Assertions.assertEquals(projectDto.getName(),project.getName());
        Assertions.assertEquals(projectDto.getStartDate(),project.getStartDate());
        Assertions.assertEquals(projectDto.getEndDate(),project.getEndDate());

    }
    public ProjectDto createValidProjectDto(){
     return ProjectDto.builder().
                id(1L).
                name("name").
                startDate(LocalDate.of(2020,01,01)).
                endDate(LocalDate.of(2099,01,01)).
                owner(1L).
                build();
    }
    public User createValidUser(){
        return User.builder().
                id(1L).
                firstName("alex").
                lastName("dupont").
                email("alexdupont@dom.fr").
                password("hash").build();
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
    public Project createValidProject(User owner){
        return Project.builder().
                name("project").
                startDate(LocalDate.of(2020,12,12)).
                endDate(LocalDate.of(2099,12,12)).
                owner(owner).
                build();
    }
}

