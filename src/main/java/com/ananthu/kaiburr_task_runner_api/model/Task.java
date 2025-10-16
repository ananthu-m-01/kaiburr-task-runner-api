package com.ananthu.kaiburr_task_runner_api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private String id;

    @NotBlank(message = "Task name cannot be empty")
    @Size(max = 100,message = "Task name should not exceed 100 characters")
    private String name;

    @NotBlank(message = "owner is required")
    private String owner;

    @NotBlank(message = "command is required")
    private String command;

//    one-to-many relationship no need to specify in mongodb[use @OneToMany annotation in relational databases
    @Valid
    private List<TaskExecution> taskExecutions = new ArrayList<>();
}
