package com.ananthu.kaiburr_task_runner_api.dto.task;

import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import com.ananthu.kaiburr_task_runner_api.model.TaskExecutionModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private String id;
    private String name;
    private String owner;
    private String command;
    private List<TaskExecutionDTO> taskExecutions;
}
