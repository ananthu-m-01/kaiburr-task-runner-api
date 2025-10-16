package com.ananthu.kaiburr_task_runner_api.dto.task;

import com.ananthu.kaiburr_task_runner_api.dto.task_execution.TaskExecutionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDTO {
    private String id;
    private String name;
    private String owner;
    private String command;
    private List<TaskExecutionDTO> taskExecutions;
}
