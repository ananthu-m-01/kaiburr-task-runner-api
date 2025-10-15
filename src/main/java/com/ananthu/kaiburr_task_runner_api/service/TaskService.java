package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskInvalidCredentialException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskNotFoundException;
import com.ananthu.kaiburr_task_runner_api.model.TaskModel;
import com.ananthu.kaiburr_task_runner_api.model.TaskStatus;
import com.ananthu.kaiburr_task_runner_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

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

        TaskModel taskModel = new TaskModel();
        taskModel.setName(createTaskDTO.getName());
        taskModel.setOwner(createTaskDTO.getOwner());
        taskModel.setCommand(createTaskDTO.getCommand());
        taskModel.setTaskExecutions(new ArrayList<>());


        TaskModel savedTask = taskRepository.save(taskModel);

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
        List<TaskModel> taskModels = taskRepository.findAll();

        List<TaskResponseDTO> responseList = taskModels.stream().map(task -> {
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

        TaskModel taskModel = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id :"+id));

        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(taskModel.getId());
        response.setName(taskModel.getName());
        response.setOwner(taskModel.getOwner());
        response.setCommand(taskModel.getCommand());

//        Task execution model - (mapping to) - task execution dto

        List<TaskExecutionDTO> executionDTOS = taskModel.getTaskExecutions()
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
        return null;
    }

    @Override
    public TaskResponseDTO updateTask(String id, UpdateTaskDTO updateTaskDTO) {
        TaskModel task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id : "+id));

        if(updateTaskDTO.getName() == null || updateTaskDTO.getName().isEmpty()){
            throw new TaskInvalidCredentialException("Task name cannot be empty");
        }
        if(updateTaskDTO.getOwner() == null || updateTaskDTO.getOwner().isEmpty()){
            throw new TaskInvalidCredentialException("Task owner name cannot be empty");
        }
        if(updateTaskDTO.getCommand() == null || updateTaskDTO.getCommand().isEmpty()){
            throw new TaskInvalidCredentialException("Task command cannot be empty");
        }

        task.setName(updateTaskDTO.getName());
        task.setCommand(updateTaskDTO.getCommand());
        task.setOwner(updateTaskDTO.getOwner());

        // Save the updated task
        TaskModel updatedTask = taskRepository.save(task);

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
        TaskModel deletedTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id :"+id));
        taskRepository.delete(deletedTask);
        return "task with id "+id+" deleted successfully";
    }

    @Override
    public List<TaskExecutionDTO> getTaskExecution(String id) {
        TaskModel task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with the id : " + id));

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
}
