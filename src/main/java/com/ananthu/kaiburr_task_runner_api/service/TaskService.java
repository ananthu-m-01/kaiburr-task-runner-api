package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.InvalidCommandException;
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
        if (!Validation.isCommandSafe(createTaskDTO.getCommand())) {
            throw new InvalidCommandException("Unsafe or potentially malicious command detected!");
        }

        Task task = Task.builder()
                .name(createTaskDTO.getName())
                .owner(createTaskDTO.getOwner())
                .command(createTaskDTO.getCommand())
                .taskExecutions(new ArrayList<>())
                .build();


        Task savedTask = taskRepository.save(task);

        return TaskResponseDTO.builder()
                .id(savedTask.getId())
                .name(savedTask.getName())
                .owner(savedTask.getOwner())
                .command(savedTask.getCommand())
                .taskExecutions(new ArrayList<>())
                .build();
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream().map(task -> {
            List<TaskExecutionDTO> executionDTOs = task.getTaskExecutions().stream()
                    .map(taskExecution -> TaskExecutionDTO.builder()
                            .startTime(taskExecution.getStartTime())
                            .endTime(taskExecution.getEndTime())
                            .output(taskExecution.getOutput())
                            .status(taskExecution.getStatus() != null ? taskExecution.getStatus() : TaskStatus.PENDING)
                            .build()
                    ).toList();

            return TaskResponseDTO.builder()
                    .id(task.getId())
                    .name(task.getName())
                    .owner(task.getOwner())
                    .command(task.getCommand())
                    .taskExecutions(executionDTOs)
                    .build();
        }).toList();

    }

    @Override
    public TaskResponseDTO getTaskById(String id) {

        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id :"+id));

        List<TaskExecutionDTO> executionDTOs = task.getTaskExecutions()
                .stream()
                .map(taskExecution -> TaskExecutionDTO.builder()
                        .startTime(taskExecution.getStartTime())
                        .endTime(taskExecution.getEndTime())
                        .output(taskExecution.getOutput())
                        .status(taskExecution.getStatus() != null ? taskExecution.getStatus() : TaskStatus.PENDING)
                        .build()
                ).toList();

        return TaskResponseDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .owner(task.getOwner())
                .command(task.getCommand())
                .taskExecutions(executionDTOs)
                .build();
    }

    @Override
    public TaskExecutionDTO runTask(String id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        if (!Validation.isCommandSafe(task.getCommand())) {
            throw new InvalidCommandException("Command is unsafe or not allowed: " + task.getCommand());
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

        TaskExecution execution = TaskExecution.builder()
                .startTime(start)
                .endTime(end)
                .output(output.toString().trim())
                .status(status)
                .build();

        task.getTaskExecutions().add(execution);
        taskRepository.save(task);

        return TaskExecutionDTO.builder()
                .startTime(execution.getStartTime())
                .endTime(execution.getEndTime())
                .output(execution.getOutput())
                .status(execution.getStatus())
                .build();
    }


    @Override
    public TaskResponseDTO updateTask(String id, UpdateTaskDTO updateTaskDTO) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("task not found with id: " + id));

        if (!Validation.isCommandSafe(updateTaskDTO.getCommand())) {
            throw new InvalidCommandException("Unsafe or potentially malicious command detected!");
        }

        task.setName(updateTaskDTO.getName());
        task.setCommand(updateTaskDTO.getCommand());
        task.setOwner(updateTaskDTO.getOwner());

        Task updatedTask = taskRepository.save(task);

        List<TaskExecutionDTO> executionDTOs = updatedTask.getTaskExecutions().stream()
                .map(execution -> TaskExecutionDTO.builder()
                        .startTime(execution.getStartTime())
                        .endTime(execution.getEndTime())
                        .output(execution.getOutput())
                        .status(execution.getStatus() != null ? execution.getStatus() : TaskStatus.PENDING)
                        .build()
                ).toList();


        return TaskResponseDTO.builder()
                .id(updatedTask.getId())
                .name(updatedTask.getName())
                .owner(updatedTask.getOwner())
                .command(updatedTask.getCommand())
                .taskExecutions(executionDTOs)
                .build();
    }


    @Override
    public String deleteTask(String id) {
        Task deletedTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id :"+id));
        taskRepository.delete(deletedTask);
        return "task with id "+id+" deleted successfully";
    }

    @Override
    public List<TaskExecutionDTO> getTaskExecution(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("task not found with the id : " + id));

        if (task.getTaskExecutions() == null || task.getTaskExecutions().isEmpty()) {
            return new ArrayList<>();
        }

        return task.getTaskExecutions()
                .stream()
                .map(execution -> TaskExecutionDTO.builder()
                        .startTime(execution.getStartTime())
                        .endTime(execution.getEndTime())
                        .output(execution.getOutput())
                        .status(execution.getStatus() != null ? execution.getStatus() : TaskStatus.PENDING)
                        .build()
                ).toList();
    }


    @Override
    public List<TaskResponseDTO> getTaskByName(String name) {
        List<Task> tasks = taskRepository.findByNameContainingIgnoreCase(name);

        return tasks.stream()
                .map(task -> {
                    List<TaskExecutionDTO> executionDTOs = task.getTaskExecutions()
                            .stream()
                            .map(exec -> TaskExecutionDTO.builder()
                                    .startTime(exec.getStartTime())
                                    .endTime(exec.getEndTime())
                                    .output(exec.getOutput())
                                    .status(exec.getStatus() != null ? exec.getStatus() : TaskStatus.PENDING)
                                    .build()
                            ).toList();

                    return TaskResponseDTO.builder()
                            .id(task.getId())
                            .name(task.getName())
                            .owner(task.getOwner())
                            .command(task.getCommand())
                            .taskExecutions(executionDTOs)
                            .build();
                }).toList();
    }
}
