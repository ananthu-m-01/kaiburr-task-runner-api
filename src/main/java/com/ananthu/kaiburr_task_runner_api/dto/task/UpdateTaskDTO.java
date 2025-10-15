package com.ananthu.kaiburr_task_runner_api.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskDTO {
    private String name;
    private String owner;
    private String command;
}
