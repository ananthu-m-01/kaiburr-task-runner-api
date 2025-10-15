package com.ananthu.kaiburr_task_runner_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskExecution {

    private Instant startTime;
    private Instant endTime;
    private String output;
    private TaskStatus status;

}
