package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.InvalidCommandException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskInvalidCredentialException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskNotFoundException;
import com.ananthu.kaiburr_task_runner_api.model.Task;
import com.ananthu.kaiburr_task_runner_api.model.TaskExecution;
import com.ananthu.kaiburr_task_runner_api.model.TaskStatus;
import com.ananthu.kaiburr_task_runner_api.repository.TaskRepository;
import com.ananthu.kaiburr_task_runner_api.util.Validation;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService implements ITaskService{

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
        if(createTaskDTO.getName() == null || createTaskDTO.getName().isEmpty()){
            throw new TaskInvalidCredentialException("Task name cannot be empty");
        }
        if(createTaskDTO.getOwner() == null || createTaskDTO.getOwner().isEmpty()){
            throw new TaskInvalidCredentialException("Task owner name cannot be empty");
        }
        if(createTaskDTO.getCommand() == null || createTaskDTO.getCommand().isEmpty()){
            throw new TaskInvalidCredentialException("Task command cannot be empty");
        }
        if (!Validation.isCommandSafe(createTaskDTO.getCommand())) {
            throw new InvalidCommandException("Unsafe or potentially malicious command detected!");
        }

        Task task = new Task();
        task.setName(createTaskDTO.getName());
        task.setOwner(createTaskDTO.getOwner());
        task.setCommand(createTaskDTO.getCommand());
        task.setTaskExecutions(new ArrayList<>());


        Task savedTask = taskRepository.save(task);

        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(savedTask.getId());
        response.setName(savedTask.getName());
        response.setOwner(savedTask.getOwner());
        response.setCommand(savedTask.getCommand());
        response.setTaskExecutions(new ArrayList<>());

        return response;
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        List<TaskResponseDTO> responseList = tasks.stream().map(task -> {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setOwner(task.getOwner());
            dto.setCommand(task.getCommand());

            // Map executions
            List<TaskExecutionDTO> executionDTOs = task.getTaskExecutions()
                    .stream()
                    .map(exec -> new TaskExecutionDTO(
                            exec.getStartTime(),
                            exec.getEndTime(),
                            exec.getOutput(),
                            exec.getStatus() != null ? exec.getStatus() : TaskStatus.PENDING
                    ))
                    .toList();

            dto.setTaskExecutions(executionDTOs);

            return dto;
        }).toList();

        return responseList;
    }

    @Override
    public TaskResponseDTO getTaskById(String id) {

        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id :"+id));

        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(task.getId());
        response.setName(task.getName());
        response.setOwner(task.getOwner());
        response.setCommand(task.getCommand());

//        Task execution model - (mapping to) - task execution dto

        List<TaskExecutionDTO> executionDTOS = task.getTaskExecutions()
                .stream()
                .map(execution -> new TaskExecutionDTO(
                        execution.getStartTime(),
                        execution.getEndTime(),
                        execution.getOutput(),
                        execution.getStatus() != null ? execution.getStatus() : TaskStatus.PENDING
                ))
                .toList();


        response.setTaskExecutions(executionDTOS);

        return response;
    }

    @Override
    public TaskExecutionDTO runTask(String id) {
        // Fetch task by ID
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        // Validate the command before execution
        if (!Validation.isCommandSafe(task.getCommand())) {
            throw new TaskInvalidCredentialException("Command is unsafe or not allowed: " + task.getCommand());
        }

        Instant start = Instant.now();
        String[] parts = task.getCommand().split("\\s+");

        StringBuilder output = new StringBuilder();
        TaskStatus status = TaskStatus.RUNNING;

        try {
            ProcessBuilder pb = new ProcessBuilder(parts);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            status = (exitCode == 0) ? TaskStatus.SUCCESS : TaskStatus.FAILED;

        } catch (Exception e) {
            output.append("Error: ").append(e.getMessage());
            status = TaskStatus.FAILED;
        }

        Instant end = Instant.now();

        // Create a new TaskExecution object and add it to the task
        TaskExecution execution = new TaskExecution(start, end, output.toString().trim(), status);
        task.getTaskExecutions().add(execution);

        // Save updated task to DB
        taskRepository.save(task);

        // Map to DTO and return
        return new TaskExecutionDTO(execution.getStartTime(), execution.getEndTime(), execution.getOutput(), execution.getStatus());
    }


    @Override
    public TaskResponseDTO updateTask(String id, UpdateTaskDTO updateTaskDTO) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id : "+id));

        if(updateTaskDTO.getName() == null || updateTaskDTO.getName().isEmpty()){
            throw new TaskInvalidCredentialException("Task name cannot be empty");
        }
        if(updateTaskDTO.getOwner() == null || updateTaskDTO.getOwner().isEmpty()){
            throw new TaskInvalidCredentialException("Task owner name cannot be empty");
        }
        if(updateTaskDTO.getCommand() == null || updateTaskDTO.getCommand().isEmpty()){
            throw new TaskInvalidCredentialException("Task command cannot be empty");
        }

        if (!Validation.isCommandSafe(updateTaskDTO.getCommand())) {
            throw new InvalidCommandException("Unsafe or potentially malicious command detected!");
        }

        task.setName(updateTaskDTO.getName());
        task.setCommand(updateTaskDTO.getCommand());
        task.setOwner(updateTaskDTO.getOwner());

        // Save the updated task
        Task updatedTask = taskRepository.save(task);

        // Map to response DTO
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(updatedTask.getId());
        response.setName(updatedTask.getName());
        response.setOwner(updatedTask.getOwner());
        response.setCommand(updatedTask.getCommand());

        List<TaskExecutionDTO> executionDTOs = updatedTask.getTaskExecutions()
                .stream()
                .map(execution -> new TaskExecutionDTO(
                        execution.getStartTime(),
                        execution.getEndTime(),
                        execution.getOutput(),
                        execution.getStatus() != null ? execution.getStatus() : TaskStatus.PENDING
                ))
                .toList();

        response.setTaskExecutions(executionDTOs);

        return response;
    }

    @Override
    public String deleteTask(String id) {
        Task deletedTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id :"+id));
        taskRepository.delete(deletedTask);
        return "task with id "+id+" deleted successfully";
    }

    @Override
    public List<TaskExecutionDTO> getTaskExecution(String id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with the id : " + id));

        if(task.getTaskExecutions() == null || task.getTaskExecutions().isEmpty()){
            return new ArrayList<>();
        }

        List<TaskExecutionDTO> taskExecutions = task.getTaskExecutions().stream()
                .map(execution -> new TaskExecutionDTO(
                        execution.getStartTime(),
                        execution.getEndTime(),
                        execution.getOutput(),
                        execution.getStatus() != null ? execution.getStatus() : TaskStatus.PENDING
                )).toList();

        return taskExecutions;
    }

    @Override
    public List<TaskResponseDTO> getTaskByName(String name) {
        List<Task> tasks = taskRepository.findByNameContainingIgnoreCase(name);

        List<TaskResponseDTO> responseList = tasks.stream().map(task -> {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setOwner(task.getOwner());
            dto.setCommand(task.getCommand());

            // Map executions
            List<TaskExecutionDTO> executionDTOs = task.getTaskExecutions()
                    .stream()
                    .map(exec -> new TaskExecutionDTO(
                            exec.getStartTime(),
                            exec.getEndTime(),
                            exec.getOutput(),
                            exec.getStatus() != null ? exec.getStatus() : TaskStatus.PENDING
                    ))
                    .toList();

            dto.setTaskExecutions(executionDTOs);

            return dto;
        }).toList();
        return responseList;
    }
}
