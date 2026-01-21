package org.example.projecthubbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String avatarUrl;

    @Builder.Default
    @Valid
    @OneToMany(mappedBy = "owner",cascade ={CascadeType.MERGE,CascadeType.PERSIST})
    private List<Project> projects=new ArrayList<>();

    @NotNull
    private List<String> roles=new ArrayList<>();

    @Builder.Default
    @Valid
    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTasks=new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public void addProject(Project project){
        if(!projects.contains(project)){
            projects.add(project);
            project.setOwner(this);
        }
    }
    public void removeProject(Project project){
        projects.remove(project);
        project.setOwner(null);
    }
    public void addAssignedTask(Task task){
        if(!assignedTasks.contains(task)){
            assignedTasks.add(task);
            task.setAssignee(this);
        }
    }
    public void removeAssignedTask(Task task){
        assignedTasks.remove(task);
        task.setAssignee(null);
    }
}

