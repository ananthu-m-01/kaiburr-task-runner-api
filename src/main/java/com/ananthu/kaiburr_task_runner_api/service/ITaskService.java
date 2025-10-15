package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITaskService {
    ResponseEntity<TaskResponseDTO> createTask(CreateTaskDTO createTaskDTO);
    ResponseEntity<List<TaskResponseDTO>> getAllTasks();
    ResponseEntity<TaskResponseDTO> getTaskById(String id);
    ResponseEntity<TaskExecutionDTO> runTask(String id);
    ResponseEntity<TaskResponseDTO> updateTask(String id, UpdateTaskDTO updateTaskDTO);
    ResponseEntity<String> deleteTask(String id);
    ResponseEntity<List<TaskExecutionDTO>> getTaskExecution(String id);
}
