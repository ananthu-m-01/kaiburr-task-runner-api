package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;

import java.util.List;

public interface ITaskService {
    TaskResponseDTO createTask(CreateTaskDTO createTaskDTO);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO getTaskById(String id);
    TaskExecutionDTO runTask(String id);
    TaskResponseDTO updateTask(String id, UpdateTaskDTO updateTaskDTO);
    String deleteTask(String id);
    List<TaskExecutionDTO> getTaskExecution(String id);
    List<TaskResponseDTO> getTaskByName(String name);
}
