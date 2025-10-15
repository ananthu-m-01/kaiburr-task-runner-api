package com.ananthu.kaiburr_task_runner_api.dto.task_execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskExecutionDTO {
    private Instant startTime;
    private Instant endTime;
    private String output;
}
