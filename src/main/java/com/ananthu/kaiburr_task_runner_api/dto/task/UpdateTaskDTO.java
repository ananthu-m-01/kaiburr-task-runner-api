package com.ananthu.kaiburr_task_runner_api.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTaskDTO {
    @NotBlank(message = "Task name cannot be empty")
    @Size(max = 100,message = "Task name should not be exceed 100 characters")
    private String name;

    @NotBlank(message = "owner is required")
    private String owner;

    @NotBlank(message = "command is required")
    private String command;
}
