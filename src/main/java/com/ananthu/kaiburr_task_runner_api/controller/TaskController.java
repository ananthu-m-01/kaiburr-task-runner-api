package com.ananthu.kaiburr_task_runner_api.controller;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO){
        TaskResponseDTO taskResponseDTO = taskService.createTask(createTaskDTO).getBody();
        return ResponseEntity.status(201).body(taskResponseDTO);
    }
}
