package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService{
    @Override
    public ResponseEntity<TaskResponseDTO> createTask(CreateTaskDTO createTaskDTO) {
        return null;
    }

    @Override
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return null;
    }

    @Override
    public ResponseEntity<TaskResponseDTO> getTaskById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<TaskExecutionDTO> runTask(String id) {
        return null;
    }

    @Override
    public ResponseEntity<TaskResponseDTO> updateTask(String id, UpdateTaskDTO updateTaskDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteTask(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<TaskExecutionDTO>> getTaskExecution(String id) {
        return null;
    }
}
