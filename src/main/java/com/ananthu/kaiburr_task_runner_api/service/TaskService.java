package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService{


    @Override
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
        return null;
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        return List.of();
    }

    @Override
    public TaskResponseDTO getTaskById(String id) {
        return null;
    }

    @Override
    public TaskExecutionDTO runTask(String id) {
        return null;
    }

    @Override
    public TaskResponseDTO updateTask(String id, UpdateTaskDTO updateTaskDTO) {
        return null;
    }

    @Override
    public String deleteTask(String id) {
        return "";
    }

    @Override
    public List<TaskExecutionDTO> getTaskExecution(String id) {
        return List.of();
    }
}
