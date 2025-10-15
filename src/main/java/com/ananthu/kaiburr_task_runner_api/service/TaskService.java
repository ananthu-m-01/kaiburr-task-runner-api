package com.ananthu.kaiburr_task_runner_api.service;

import com.ananthu.kaiburr_task_runner_api.dto.task.CreateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.TaskResponseDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task.UpdateTaskDTO;
import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskInvalidCredentialException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskNotFoundException;
import com.ananthu.kaiburr_task_runner_api.model.TaskModel;
import com.ananthu.kaiburr_task_runner_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

        TaskModel savedTask = taskRepository.save(taskModel);

        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(savedTask.getId());
        response.setName(savedTask.getName());
        response.setOwner(savedTask.getOwner());
        response.setCommand(savedTask.getCommand());

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
            return dto;
        }).toList();

        return responseList;
    }


    @Override
    public TaskResponseDTO getTaskById(String id) {

        TaskModel taskModel = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("task not found with id "+id));

        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(taskModel.getId());
        response.setName(taskModel.getName());
        response.setOwner(taskModel.getOwner());
        response.setCommand(taskModel.getCommand());
        response.setTaskExecutions(taskModel.getTaskExecutions());


        return response;
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
