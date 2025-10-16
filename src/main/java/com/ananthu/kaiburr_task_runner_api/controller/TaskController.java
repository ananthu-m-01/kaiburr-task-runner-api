package com.ananthu.kaiburr_task_runner_api.controller;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import com.ananthu.kaiburr_task_runner_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO){
        TaskResponseDTO taskResponseDTO = taskService.createTask(createTaskDTO);
        return ResponseEntity.status(201).body(taskResponseDTO);
    }


    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(){
        List<TaskResponseDTO> response = taskService.getAllTasks();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable String id){
        TaskResponseDTO response = taskService.getTaskById(id);
        if(response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/run")
    public ResponseEntity<TaskExecutionDTO> runTask(@PathVariable String id){
        TaskExecutionDTO execution = taskService.runTask(id);
        if(execution == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(execution);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable String id,@Valid @RequestBody UpdateTaskDTO updateTaskDTO){
            TaskResponseDTO updated = taskService.updateTask(id,updateTaskDTO);
            if(updated == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id){
        String result = taskService.deleteTask(id);
        if (result == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}/executions")
    public ResponseEntity<List<TaskExecutionDTO>> getTaskExecutions(@PathVariable String id) {
        List<TaskExecutionDTO> executions = taskService.getTaskExecution(id);
        if (executions == null || executions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(executions);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasksByName(@PathVariable String name){
        List<TaskResponseDTO> response = taskService.getTaskByName(name);
        return ResponseEntity.ok(response);
    }
}
