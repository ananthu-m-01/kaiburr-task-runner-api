package com.ananthu.kaiburr_task_runner_api.model;

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
    private String name;
    private String owner;
    private String command;

//    one-to-many relationship no need to specify in mongodb[use @OneToMany annotation in relational databases
    private List<TaskExecution> taskExecutions = new ArrayList<>();
}
